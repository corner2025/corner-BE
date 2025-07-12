// src/main/java/com/corner/travel/dutyfree/controller/DutyFreeProductController.java
package com.corner.travel.dutyfree.controller;

import com.corner.travel.dutyfree.dto.RankingDto;
import com.corner.travel.dutyfree.service.DutyFreeProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dutyfree/products")
@RequiredArgsConstructor
public class DutyFreeProductController {

    private final DutyFreeProductService service;

    @Operation(summary = "월간 판매량 랭킹 조회",
            description = "yearMonth(yyyy-MM) 기준으로 카테고리별 판매량 합계 내림차순, 상위 limit개 반환")
    @GetMapping("/monthly-ranking")
    public List<RankingDto> getMonthlyRanking(
            @Parameter(description = "조회할 월 (format: yyyy-MM)", required = true)
            @RequestParam String yearMonth,
            @Parameter(description = "상위 N개 (default=10)")
            @RequestParam(defaultValue = "10") int limit
    ) {
        return service.getMonthlyRanking(yearMonth, limit);
    }

    @Operation(summary = "연간 판매량 랭킹 조회",
            description = "year(yyyy) 기준으로 카테고리별 판매량 합계 내림차순, 상위 limit개 반환")
    @GetMapping("/annual-ranking")
    public List<RankingDto> getAnnualRanking(
            @Parameter(description = "조회할 연도 (format: yyyy)", required = true)
            @RequestParam String year,
            @Parameter(description = "상위 N개 (default=10)")
            @RequestParam(defaultValue = "10") int limit
    ) {
        return service.getAnnualRanking(year, limit);
    }
}