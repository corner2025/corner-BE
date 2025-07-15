package com.corner.travel.performance.api.controller;


import com.corner.travel.performance.api.dto.PerformanceApiListResponse;
import com.corner.travel.performance.api.service.PerformanceAPiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceApiController {

        private final PerformanceAPiService performanceAPiService;

        @GetMapping("/test")
        public  PerformanceApiListResponse getPerformanceList(
                @RequestParam(defaultValue = "20250101") String startDate,
                @RequestParam(defaultValue = "20251231") String endDate,
                @RequestParam(defaultValue = "1") int page,
                @RequestParam(defaultValue = "100") int rows
        ){
            return performanceAPiService.fetchAndParsePerformanceList(startDate, endDate, page, rows);
        }
}
