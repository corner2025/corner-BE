package com.corner.travel.festival.controller;

import com.corner.travel.festival.dto.FestivalDto;
import com.corner.travel.festival.service.FestivalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/festivals")
@CrossOrigin(origins = "*")
public class FestivalController {
    private final FestivalService service;

    public FestivalController(FestivalService service) {
        this.service = service;
    }

    /** 축제 목록 조회 (페이징) */
    @GetMapping
    public ResponseEntity<List<FestivalDto>> list(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows
    ) {
        return ResponseEntity.ok(service.getFestivalList(pageNo, numOfRows));
    }

    /** 축제 상세 조회 (이름 기반) */
    @GetMapping("/{festivalName}")
    public ResponseEntity<FestivalDto> detail(@PathVariable String festivalName) {
        return service.getFestivalDetail(festivalName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
