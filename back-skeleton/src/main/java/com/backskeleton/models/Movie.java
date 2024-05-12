package com.backskeleton.models;
import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "director")
    private String director;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "synopsis", columnDefinition = "TEXT")
    private String synopsis;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "updated_at", nullable = true, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NULL")
    private Timestamp updatedAt;

    public Timestamp getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    // Getters and setters

    public Movie() {
    }

    public Movie(Long id, String title, String director, Date releaseDate, String synopsis, String posterUrl, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        this.synopsis = synopsis;
        this.posterUrl = posterUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterUrl() {
        return this.posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Movie id(Long id) {
        setId(id);
        return this;
    }

    public Movie title(String title) {
        setTitle(title);
        return this;
    }

    public Movie director(String director) {
        setDirector(director);
        return this;
    }

    public Movie releaseDate(Date releaseDate) {
        setReleaseDate(releaseDate);
        return this;
    }

    public Movie synopsis(String synopsis) {
        setSynopsis(synopsis);
        return this;
    }

    public Movie posterUrl(String posterUrl) {
        setPosterUrl(posterUrl);
        return this;
    }

    public Movie createdAt(Timestamp createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", director='" + getDirector() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", synopsis='" + getSynopsis() + "'" +
            ", posterUrl='" + getPosterUrl() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
    
}
