package com.corner.travel.touristSpot.repository;

import com.corner.travel.touristSpot.domain.TouristSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
    //API 매핑, 호출 --> DB 저장(tourist_spot)
    Optional<TouristSpot> findByContentid(String contentid); // 중복 저장 방지용

    //API 기반 관광리스트 조회
    Page<TouristSpot> findByTitleContaining(String keyword, Pageable pageable);

   // areaCode -> areaName mapping
    Page<TouristSpot> findByAreacode(String areacode, Pageable pageable);

    //keyword + areaName Search
    Page<TouristSpot> findByTitleContainingAndAreacode(String title, String areacode, Pageable pageable);

    // Page<TouristSpot> findByTitleContainingAndAreacodeAndSigungucode(String title, String areacode, String sigungucode, Pageable pageable);

    Optional<TouristSpot> findByMapxAndMapy(String mapx, String mapy);


}
