package com.backskeleton.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backskeleton.models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}

