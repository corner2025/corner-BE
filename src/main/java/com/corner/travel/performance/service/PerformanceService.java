package com.corner.travel.performance.service;


import com.corner.travel.performance.api.dto.PerformanceApiDto;
import com.corner.travel.performance.api.service.PerformanceAPiService;
import com.corner.travel.performance.domain.Performance;
import com.corner.travel.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    //DB API호출 기반으로 공연 정보 저장
    public void savePerformances(List<PerformanceApiDto> performanceDtos) {
        // 1. API에서 받은 ID 리스트
        List<String> incomingIds = performanceDtos.stream()
                .map(PerformanceApiDto::getId)
                .toList();

        // ✨ 오류 방지: 타입이 맞는지 확인하고 그대로 넘기기
        List<Performance> existingPerformances = performanceRepository.findAllById(incomingIds);
        Set<String> existingIds = existingPerformances.stream()
                .map(Performance::getId)
                .collect(Collectors.toSet());

        List<Performance> newPerformances = performanceDtos.stream()
                .filter(dto -> !existingIds.contains(dto.getId()))
                .map(dto -> Performance.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .place(dto.getPlace())
                        .posterUrl(dto.getPosterUrl())
                        .area(dto.getArea())
                        .genre(dto.getGenre())
                        .openRun(dto.getOpenRun())
                        .state(dto.getState())
                        .build())
                .toList();

        performanceRepository.saveAll(newPerformances);
    }

    //DB 기반 정보조회 , 페이징
    public Page<Performance> getPerformances(String area, String startDate, String endDate, Pageable pageable) {
        return performanceRepository.searchPerformances(area, startDate, endDate, pageable);
    }
    //상세조회
    public Performance getPerformanceById(String id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 존재하지 않습니다. id=" + id));
    }

}
