package com.backskeleton.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backskeleton.dao.PlaceRepository;
import com.backskeleton.dao.ReviewRepository;
import com.backskeleton.models.Place;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import com.backskeleton.services.FileStorageService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;
     @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FileStorageService fileStorageService;
     @PersistenceContext
    private EntityManager entityManager;

    // Endpoint pour récupérer toutes les places
    @GetMapping
    public List<Place> getAllPlaces() {
        // Récupérer tous les lieux
        List<Place> places = placeRepository.findAll();

        // Trier les lieux par ordre décroissant des ID
        List<Place> sortedPlaces = places.stream()
                .sorted((p1, p2) -> Long.compare(p2.getId(), p1.getId())) // Tri décroissant des ID
                .collect(Collectors.toList());

        // Retourner les lieux triés
        return sortedPlaces;
    }

    // Endpoint pour récupérer une place par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable("id") Long id) {
        Optional<Place> place = placeRepository.findById(id);
        return place.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint pour créer une nouvelle place
    @PostMapping(consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Place> createPlace(@RequestPart(name = "place") Place place,
            @RequestPart(name = "imageUrl", required = false) MultipartFile file) {
        place.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        place.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // Si un fichier est fourni, associez-le à l'utilisateur
        if (file != null && !file.isEmpty()) {
            try {
                // Sauvegardez le fichier et récupérez son URL
                String fileUrl = fileStorageService.storeFile(file, "places/");

                // Associez l'URL du fichier à l'utilisateur

                place.setImageUrl("/assets/images/places/" + fileUrl);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Place savedPlace = placeRepository.save(place);
        return new ResponseEntity<>(savedPlace, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @RequestPart("place") Place newPlace,
            @RequestPart(value = "imageUrl", required = false) MultipartFile file) {
    
        Optional<Place> optionalPlace = placeRepository.findById(id);
    
        if (optionalPlace.isPresent()) {
            Place existingPlace = optionalPlace.get();
            existingPlace.setTitle(newPlace.getTitle());
            existingPlace.setAddress(newPlace.getAddress());
            existingPlace.setOpeningHours(newPlace.getOpeningHours());
            existingPlace.setUpdatedAt(newPlace.getUpdatedAt());
    
            try {
                String fileUrl = null;
    
                if (file != null && !file.isEmpty()) {
                    // Sauvegarder le fichier et récupérer son URL
                    if (StringUtils.isNotBlank(existingPlace.getImageUrl())) {
                        fileUrl = fileStorageService.updateFile(existingPlace.getImageUrl(), file, "places/");
                    } else {
                        fileUrl = fileStorageService.storeFile(file, "places/");
                    }
                    
                    // Associer l'URL du fichier à l'utilisateur
                    existingPlace.setImageUrl("/assets/images/places/" + fileUrl);
                }
    
                // Enregistrer l'utilisateur mis à jour dans la base de données
                Place updatedPlace = placeRepository.save(existingPlace);
                return new ResponseEntity<>(updatedPlace, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Endpoint pour supprimer une place
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deletePlace(@PathVariable("id") Long id) {
        try {
            Optional<Place> optionalPlace = Optional.ofNullable(entityManager.find(Place.class, id));

            if (optionalPlace.isPresent()) {
                Place place = optionalPlace.get();

                // Supprimer tous les avis associés au lieu
                reviewRepository.deleteByEntityIdAndEntityType(id, "place");

                // Supprimer l'image du lieu s'il en a une
                if (StringUtils.isNotBlank(place.getImageUrl())) {
                    fileStorageService.deleteFile(place.getImageUrl());
                }

                // Supprimer le lieu de la base de données
                entityManager.remove(place);

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
