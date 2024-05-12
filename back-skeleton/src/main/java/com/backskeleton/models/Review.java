package com.backskeleton.models;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;


    @Column(name = "full_name", nullable = false)
    private String full_name;

    @Column(name = "email", nullable = false)
    private String  email;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    
    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "summary")
    private String summary;

    @Column(name = "review")
    private String review;

    @Column(name = "updated_at", nullable = true, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NULL")
    private Timestamp updatedAt;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    // Ajoutez un attribut pour stocker l'entité associée (Movie ou Place)
    @Transient
    private Object entity;



    public String getFull_name() {
        return this.full_name;
    }
    
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getters and setters

    public Review() {
    }

    public Review(Long id, User user, Long entityId, String entityType, int rating, String summary, String review, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.entityId = entityId;
        this.entityType = entityType;
        this.rating = rating;
        this.summary = summary;
        this.review = review;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReview() {
        return this.review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Review id(Long id) {
        setId(id);
        return this;
    }

    public Review user(User user) {
        setUser(user);
        return this;
    }

    public Review entityId(Long entityId) {
        setEntityId(entityId);
        return this;
    }

    public Review entityType(String entityType) {
        setEntityType(entityType);
        return this;
    }

    public Review rating(int rating) {
        setRating(rating);
        return this;
    }

    public Review summary(String summary) {
        setSummary(summary);
        return this;
    }

    public Review review(String review) {
        setReview(review);
        return this;
    }

    public Review createdAt(Timestamp createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    // Méthode pour définir l'entité en fonction du type
    public void setEntity(Object entity) {
        if (entity instanceof Movie) {
            this.entity = (Movie) entity;
        } else if (entity instanceof Place) {
            this.entity = (Place) entity;
        }
    }

    // Méthode pour obtenir l'entité associée
    public Object getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", user='" + getUser() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", rating='" + getRating() + "'" +
            ", summary='" + getSummary() + "'" +
            ", review='" + getReview() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
    
}
