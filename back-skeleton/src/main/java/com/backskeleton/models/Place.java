package com.backskeleton.models;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "address")
    private String address;

    @Column(name = "image_Url")
    private String imageUrl;

    public Place(Long id, String title, String address, String imageUrl, String openingHours, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.imageUrl = imageUrl;
        this.openingHours = openingHours;
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Place imageUrl(String imageUrl) {
        setImageUrl(imageUrl);
        return this;
    }

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "updated_at", nullable = true, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NULL")
    private Timestamp updatedAt;

    
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;


    // Getters and setters

    public Place() {
    }

    public Place(Long id, String title, String address, String openingHours, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.openingHours = openingHours;
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


    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningHours() {
        return this.openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    public Timestamp getUpdatedAt() {
        return this.updatedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Place id(Long id) {
        setId(id);
        return this;
    }

    public Place title(String title) {
        setTitle(title);
        return this;
    }


    public Place address(String address) {
        setAddress(address);
        return this;
    }

    public Place openingHours(String openingHours) {
        setOpeningHours(openingHours);
        return this;
    }

    public Place createdAt(Timestamp createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", address='" + getAddress() + "'" +
            ", openingHours='" + getOpeningHours() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }

}
