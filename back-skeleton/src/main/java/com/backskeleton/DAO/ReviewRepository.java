package com.backskeleton.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backskeleton.models.Review;
import com.backskeleton.models.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEntityTypeAndEntityId(String entityType, Long entityId);
    void deleteByEntityIdAndEntityType(Long entityId, String entityType);

    void deleteByUser(User user);

    @Query("SELECT r.rating, COUNT(r) FROM Review r GROUP BY r.rating")
    List<Object[]> findReviewStatistics();

    long countByUser(User user);



}
