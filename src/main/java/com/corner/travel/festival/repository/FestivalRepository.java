// src/main/java/com/corner/travel/festival/repository/FestivalRepository.java
package com.corner.travel.festival.repository;

import com.corner.travel.festival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Long> {
    // 기본 CRUD, findAll(), findById(), saveAll() 등이 제공됩니다.
}
