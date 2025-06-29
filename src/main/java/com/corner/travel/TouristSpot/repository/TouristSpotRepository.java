package com.corner.travel.TouristSpot.repository;

import com.corner.travel.TouristSpot.domain.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
    List<TouristSpot> findByRegion(String region);
}
