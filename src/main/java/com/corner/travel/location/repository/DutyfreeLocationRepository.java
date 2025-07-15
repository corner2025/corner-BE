package com.corner.travel.location.repository;

import com.corner.travel.location.entity.DutyfreeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DutyfreeLocationRepository extends JpaRepository<DutyfreeLocation, Long> {
    // 기본 CRUD 메서드는 JpaRepository가 제공
}
