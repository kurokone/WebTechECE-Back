package com.backskeleton.controllers;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backskeleton.dao.UserRepository;
import com.backskeleton.dao.ReviewRepository;
import com.backskeleton.models.User;
import com.backskeleton.services.FileStorageService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @PersistenceContext
    private EntityManager entityManager;

    // Endpoint pour récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Récupérer tous les utilisateurs
        List<User> users = userRepository.findAll();

        // Trier les utilisateurs par ordre décroissant des ID
        List<User> sortedUsers = users.stream()
                .sorted((u1, u2) -> Long.compare(u2.getId(), u1.getId())) // Tri décroissant des ID
                .collect(Collectors.toList());

        // Retourner les utilisateurs triés
        return new ResponseEntity<>(sortedUsers, HttpStatus.OK);
    }

    // Endpoint pour récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint pour créer un nouvel utilisateur
    @PostMapping(consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<User> createUser(@RequestPart(name = "user") User user,
            @RequestPart(name = "image", required = false) MultipartFile file) {
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // Si un fichier est fourni, associez-le à l'utilisateur
        if (file != null && !file.isEmpty()) {
            try {
                // Sauvegardez le fichier et récupérez son URL
                String fileUrl = fileStorageService.storeFile(file, "users/");

                // Associez l'URL du fichier à l'utilisateur

                user.setImage("/assets/images/users/" + fileUrl);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        System.out.println(user);

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Endpoint pour mettre à jour un utilisateur existant
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestPart("user") User newUser,
            @RequestPart(value = "image", required = false) MultipartFile file) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(newUser.getFirstName());
            existingUser.setLastName(newUser.getLastName());
            existingUser.setBirthdate(newUser.getBirthdate());
            existingUser.setUsername(newUser.getUsername());
            existingUser.setEmail(newUser.getEmail());
            existingUser.setPassword(newUser.getPassword());
            existingUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            try {
                String fileUrl = null;

                if (file != null && !file.isEmpty()) {
                    // Sauvegarder le fichier et récupérer son URL
                    if (StringUtils.isNotBlank(existingUser.getImage())) {
                        fileUrl = fileStorageService.updateFile(existingUser.getImage(), file, "users/");
                    } else {
                        fileUrl = fileStorageService.storeFile(file, "users/");
                    }

                    // Associer l'URL du fichier à l'utilisateur
                    existingUser.setImage("/assets/images/users/" + fileUrl);
                }

                // Enregistrer l'utilisateur mis à jour dans la base de données
                User updatedUser = userRepository.save(existingUser);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint pour supprimer un utilisateur
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        try {
            // Chercher l'utilisateur par son ID
            User user = entityManager.find(User.class, id);

            // Vérifier si l'utilisateur existe
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Vérifier si l'utilisateur a une image non vide
            if (StringUtils.isNotBlank(user.getImage())) {
                // Supprimer l'image de l'utilisateur
                fileStorageService.deleteFile(user.getImage());
            }
            // Supprimer les avis associés à cet utilisateur
            reviewRepository.deleteByUser(user);

            // Supprimer l'utilisateur
            entityManager.remove(user);

            // Réponse en cas de succès
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // En cas d'erreur, imprimer l'exception et renvoyer une réponse avec une erreur
            // de serveur interne
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
