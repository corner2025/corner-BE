package com.corner.travel.performance.controller;

import com.corner.travel.performance.api.dto.PerformanceApiDto;
import com.corner.travel.performance.api.dto.PerformanceApiListResponse;
import com.corner.travel.performance.api.service.PerformanceAPiService;
import com.corner.travel.performance.domain.Performance;
import com.corner.travel.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceAPiService performanceApiService;
    private final PerformanceService performanceService;

    // DB저장
    @PostMapping("/init")
    public String savePerformancesToDb(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam int page,
            @RequestParam int rows) {

        // 1. API 호출 → XML 파싱
        PerformanceApiListResponse response = performanceApiService.fetchAndParsePerformanceList(startDate, endDate, page, rows);
        List<PerformanceApiDto> performanceList = response.getPerformance();

        // 2. DB 저장
        performanceService.savePerformances(performanceList);

        return "공연 데이터 저장 완료 (" + performanceList.size() + "건)";
    }


    @PostMapping("/init/all")
    public String saveAllPerformancesToDb(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        performanceService.fetchAllPerformancesByPeriod(startDate, endDate);
        return "공연 데이터 전체 수집 시작";
    }



    //DB기반 공연 전체조회
    @GetMapping
    public Page<Performance> getPerformances(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return performanceService.getPerformances(area, startDate, endDate, pageable);
    }

    //상세조회
    @GetMapping("/{id}")
    public Performance getPerformanceById(@PathVariable String id) {
        return performanceService.getPerformanceById(id);
    }

}
