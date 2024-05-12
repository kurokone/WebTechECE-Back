package com.backskeleton.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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

import com.backskeleton.dao.MovieRepository;
import com.backskeleton.dao.ReviewRepository;
import com.backskeleton.models.Movie;
import com.backskeleton.services.FileStorageService;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FileStorageService fileStorageService;

     @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public List<Movie> getAllMovies() {
        // Récupérer tous les films
        List<Movie> movies = movieRepository.findAll();

        // Trier les films par ordre décroissant des ID
        List<Movie> sortedMovies = movies.stream()
                .sorted((m1, m2) -> Long.compare(m2.getId(), m1.getId())) // Tri décroissant des ID
                .collect(Collectors.toList());

        // Retourner les films triés
        return sortedMovies;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Movie> createMovie(@RequestPart(name = "movie") Movie movie,
            @RequestPart(name = "posterUrl", required = false) MultipartFile file) {
        movie.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        movie.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // Si un fichier est fourni, associez-le à l'utilisateur
        if (file != null && !file.isEmpty()) {
            try {
                // Sauvegardez le fichier et récupérez son URL
                String fileUrl = fileStorageService.storeFile(file, "movies/");

                // Associez l'URL du fichier à l'utilisateur

                movie.setPosterUrl("/assets/images/movies/" + fileUrl);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        System.out.println(movie);

        Movie savedMovie = movieRepository.save(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestPart("movie") Movie newMovie,
            @RequestPart(value = "posterUrl", required = false) MultipartFile file) {

        Optional<Movie> optionalMovie = movieRepository.findById(id);

        if (optionalMovie.isPresent()) {
            Movie existingMovie = optionalMovie.get();
            existingMovie.setTitle(newMovie.getTitle());
            existingMovie.setDirector(newMovie.getDirector());
            existingMovie.setReleaseDate(newMovie.getReleaseDate());
            existingMovie.setSynopsis(newMovie.getSynopsis());
            existingMovie.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            try {
                String fileUrl = null;

                if (file != null && !file.isEmpty()) {
                    // Sauvegarder le fichier et récupérer son URL
                    if (StringUtils.isNotBlank(existingMovie.getPosterUrl())) {
                        fileUrl = fileStorageService.updateFile(existingMovie.getPosterUrl(), file, "movies/");
                    } else {
                        fileUrl = fileStorageService.storeFile(file, "movies/");
                    }

                    // Associer l'URL du fichier à l'utilisateur
                    existingMovie.setPosterUrl("/assets/images/movies/" + fileUrl);
                }

                // Enregistrer l'utilisateur mis à jour dans la base de données
                Movie updatedMovie = movieRepository.save(existingMovie);
                return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        try {
            Optional<Movie> optionalMovie = Optional.ofNullable(entityManager.find(Movie.class, id));

            if (!optionalMovie.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Movie existingMovie = optionalMovie.get();

            // Supprimer tous les avis associés au film
            reviewRepository.deleteByEntityIdAndEntityType(id, "movie");

            // Vérifier si le film a une image non vide
            if (StringUtils.isNotBlank(existingMovie.getPosterUrl())) {
                // Supprimer l'image du film
                fileStorageService.deleteFile(existingMovie.getPosterUrl());
            }

            entityManager.remove(existingMovie);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
