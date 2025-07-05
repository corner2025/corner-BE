package com.corner.travel.performance.repository;


import com.corner.travel.performance.domain.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, String> {

    //1. 페이징 , 필터링, 전체조회
    @Query("SELECT p FROM Performance p " +
            "WHERE (:area IS NULL OR p.area LIKE %:area%) " +
            "AND (:startDate IS NULL OR p.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR p.endDate <= :endDate)")
    Page<Performance> searchPerformances(
            @Param("area") String area,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            Pageable pageable
    );

}
