package com.corner.travel.festival.repository;

import com.corner.travel.festival.domain.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FestivalRepositoryCustom {
    Page<Festival> findByFilters(String startDate, String endDate, String location, String title, Pageable pageable);
}
