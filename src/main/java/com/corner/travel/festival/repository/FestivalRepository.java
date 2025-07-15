package com.corner.travel.festival.repository;

import com.corner.travel.festival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Long>, FestivalRepositoryCustom {
    // 기본 CRUD + 커스텀 메서드 포함
}
