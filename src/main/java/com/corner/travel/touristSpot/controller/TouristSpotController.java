package com.corner.travel.touristSpot.controller;

import com.corner.travel.touristSpot.api.dto.NearbyItem;
import com.corner.travel.touristSpot.api.service.NearbyTouristSpotApiService;
import com.corner.travel.touristSpot.dto.NearbyTouristSpotDto;
import com.corner.travel.touristSpot.dto.TouristSpotDetailResponse;
import com.corner.travel.touristSpot.dto.TouristSpotListResponse;
import com.corner.travel.touristSpot.dto.TouristSpotSearchRequest;
import com.corner.travel.touristSpot.service.TouristSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tourist-spot")
public class TouristSpotController {

    private final TouristSpotService touristSpotService;
    private final NearbyTouristSpotApiService nearbyTouristSpotApiService;

    //관광지 DB 전체 데이터 저장
    @PostMapping("/init/all")
    public ResponseEntity<?> initAllTouristSpots() {
        touristSpotService.fetchAllTouristSpots();
        return ResponseEntity.ok("초기 관광지 데이터 수집 완료");
    }
    //관광지 DB 저장 INSERT
    @PostMapping("/init")
    public ResponseEntity<String> initTouristData(){
        touristSpotService.fetchAndSaveTouristSpots();
        return ResponseEntity.ok("관광지 데이터 저장 완료");
    }

    //관광지 LIST UP , REQ(시군구, 지역 , 키워드)로 LIST UP
    @GetMapping
    public Page<TouristSpotListResponse> getTouristSpots(
            @ModelAttribute TouristSpotSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return touristSpotService.getTouristSpots(request, pageable);
    }

    //관광지 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<TouristSpotDetailResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(touristSpotService.getTouristSpotDetail(id));
    }

    //관광지 주변 정보 api 기반 조회
    @GetMapping("/{id}/nearby")
    public List<NearbyItem> getNearbySpots(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam(defaultValue = "12") int contentTypeId
    ) {
        return touristSpotService.getNearbyTouristSpots(id, radius, contentTypeId);
    }


}
