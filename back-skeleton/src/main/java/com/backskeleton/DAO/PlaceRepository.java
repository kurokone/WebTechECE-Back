package com.backskeleton.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backskeleton.models.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
