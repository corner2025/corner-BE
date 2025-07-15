package com.corner.travel.festival.controller;

import com.corner.travel.festival.dto.FestivalDto;
import com.corner.travel.festival.service.FestivalService;
import io.swagger.v3.oas.annotations.Operation;
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

    /**
     * 외부 API 기반 축제 목록 조회
     * @param pageNo 1-base 페이지 번호
     * @param numOfRows 페이지 크기
     */
    @Operation(summary = "DB에 축제정보 저장",
            description = "DB 데이터 날아갔을때 쓸것 ..")
    @GetMapping("/init")
    public ResponseEntity<List<FestivalDto>> list(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows
    ) {
        return ResponseEntity.ok(service.getFestivalList(pageNo, numOfRows));
    }

    /**
     * DB에 저장된 축제 페이징 조회
     * @param pageNo 1-base 페이지 번호
     * @param pageSize 페이지 크기
     */
    @GetMapping
    public ResponseEntity<List<FestivalDto>> listFromDb(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        List<FestivalDto> results = service.findAllFromDb(pageNo, pageSize);
        return ResponseEntity.ok(results);
    }

    // id 기반 검색
    @GetMapping("/{id}")
    public ResponseEntity<FestivalDto> detail(@PathVariable Long id) {
        return service.getFestivalDetail(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
