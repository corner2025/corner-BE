package com.corner.travel.touristSpot.service;

import com.corner.travel.touristSpot.api.dto.NearbyItem;
import com.corner.travel.touristSpot.api.dto.TouristSpotItem;
import com.corner.travel.touristSpot.api.service.NearbyTouristSpotApiService;
import com.corner.travel.touristSpot.api.service.TouristSpotApiService;
import com.corner.travel.touristSpot.domain.TouristSpot;
import com.corner.travel.touristSpot.dto.NearbyTouristSpotDto;
import com.corner.travel.touristSpot.dto.TouristSpotDetailResponse;
import com.corner.travel.touristSpot.dto.TouristSpotListResponse;
import com.corner.travel.touristSpot.dto.TouristSpotSearchRequest;
import com.corner.travel.touristSpot.repository.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TouristSpotService {

    private final TouristSpotApiService touristSpotApiService;
    private final TouristSpotRepository touristSpotRepository;
    private final NearbyTouristSpotApiService nearbyTouristSpotApiService;

    @Transactional
    public void fetchAndSaveTouristSpots() {
        // ✅ 원하는 파라미터로 API 호출 (예: 1페이지, 10개씩, 지역코드 6번, 시군구코드 10번)
        List<?> result = touristSpotApiService.getTouristSpots(1, 10, null, null, null);

        for (Object obj : result) {
            if (obj instanceof TouristSpotItem item) {
                // 중복 방지: contentid 기준으로 확인
                if (touristSpotRepository.findByContentid(item.getContentid()).isPresent()) continue;

                TouristSpot spot = TouristSpot.builder()
                        .title(item.getTitle())
                        .addr1(item.getAddr1())
                        .addr2(item.getAddr2())
                        .areacode(item.getAreacode())
                        .cat1(item.getCat1())
                        .cat2(item.getCat2())
                        .cat3(item.getCat3())
                        .contentid(item.getContentid())
                        .contenttypeid(item.getContenttypeid())
                        .firstimage(item.getFirstimage())
                        .firstimage2(item.getFirstimage2())
                        .mapx(item.getMapx())
                        .mapy(item.getMapy())
                        .sigungucode(item.getSigungucode())
                        .tel(item.getTel())
                        .zipcode(item.getZipcode())
                        .build();

                touristSpotRepository.save(spot);
            }
        }
    }

    //관광지 조회
    public Page<TouristSpotListResponse> getTouristSpots(TouristSpotSearchRequest request, Pageable pageable) {
        Page<TouristSpot> result;

        if (request.getAreaCode() != null && request.getSigunguCode() != null) {
            result = touristSpotRepository.findByTitleContainingAndAreacodeAndSigungucode(
                    request.getKeyword() != null ? request.getKeyword() : "",
                    request.getAreaCode(),
                    request.getSigunguCode(),
                    pageable
            );
        } else if (request.getKeyword() != null) {
            result = touristSpotRepository.findByTitleContaining(request.getKeyword(), pageable);
        } else {
            result = touristSpotRepository.findAll(pageable);
        }

        return result.map(touristSpot -> TouristSpotListResponse.builder()
                .id(touristSpot.getId())
                .title(touristSpot.getTitle())
                .addr1(touristSpot.getAddr1())
                .firstimage(touristSpot.getFirstimage())
                .mapx(touristSpot.getMapx())
                .mapy(touristSpot.getMapy())
                .build());
    }

    //관광지 상세조회
    public TouristSpotDetailResponse getTouristSpotDetail(Long id) {
        TouristSpot spot = touristSpotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 관광지가 없습니다. id=" + id));
        return TouristSpotDetailResponse.from(spot);
    }

    //관광지 상세조회 기반 주변 관광지 정보
    public List<NearbyItem> getNearbyTouristSpots(Long id, int radius, int contentTypeId) {
        TouristSpot spot = touristSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 관광지를 찾을 수 없습니다."));

        return nearbyTouristSpotApiService.getNearbySpots(
                spot.getMapx(),  // String
                spot.getMapy(),  // String
                radius,
                contentTypeId
        );
    }
}
