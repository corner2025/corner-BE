package com.corner.travel.location.controller;

import com.corner.travel.location.dto.DutyfreeLocationDto;
import com.corner.travel.location.service.DutyfreeLocationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dutyfree/locations")
@CrossOrigin(origins = "*")
public class DutyfreeLocationController {

    private final DutyfreeLocationService service;

    public DutyfreeLocationController(DutyfreeLocationService service) {
        this.service = service;
    }

    @Operation(summary = "면세점 위치 리스트 전체 조회")
    @GetMapping
    public ResponseEntity<List<DutyfreeLocationDto>> getAllLocations() {
        List<DutyfreeLocationDto> list = service.getAllLocations();
        return ResponseEntity.ok(list);
    }
}
