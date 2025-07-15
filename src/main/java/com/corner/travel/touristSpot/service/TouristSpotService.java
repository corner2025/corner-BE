package com.corner.travel.touristSpot.service;

import com.corner.travel.touristSpot.api.dto.AreaCodeItem;
import com.corner.travel.touristSpot.api.dto.DetailTouristSpotItem;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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

    @Transactional
    public void fetchAllTouristSpots() {
        for (int areaCode = 1; areaCode <= 39; areaCode++) {
            for (int page = 1; ; page++) {
                try {
                    List<TouristSpotItem> items = (List<TouristSpotItem>) touristSpotApiService.getTouristSpots(page, 100, String.valueOf(areaCode), null, null);
                    if (items == null || items.isEmpty()) break;

                    for (TouristSpotItem item : items) {
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
                } catch (Exception e) {
                    log.error("areaCode={} page={} 처리 중 오류 발생", areaCode, page, e);
                    break; // 이 페이지 건너뛰고 다음 지역으로 넘어감
                }
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

        Map<String, String> areaNameMap = getAreaCodeNameMap();

        return result.map(touristSpot -> {
            String areaName = areaNameMap.getOrDefault(
                    String.valueOf(touristSpot.getAreacode()), "알 수 없음");

            String cat2Name = getCat2Name(
                    touristSpot.getCat1(),
                    touristSpot.getCat2()
            );
            String cat3Name = getCat3Name(
                    touristSpot.getCat1(),
                    touristSpot.getCat2(),
                    touristSpot.getCat3()
            );

            return TouristSpotListResponse.builder()
                    .id(touristSpot.getId())
                    .title(touristSpot.getTitle())
                    .addr1(touristSpot.getAddr1())
                    .firstimage(touristSpot.getFirstimage())
                    .mapx(touristSpot.getMapx())
                    .mapy(touristSpot.getMapy())
                    .areaName(areaName)
                    .cat2Name(cat2Name)
                    .cat3Name(cat3Name)
                    .build();
        });
    }

    private Map<String, String> areaCodeNameMapCache = null;

    private Map<String, String> getAreaCodeNameMap() {
        if (areaCodeNameMapCache == null) {
            List<AreaCodeItem> items = touristSpotApiService.getAreaCodeList();
            areaCodeNameMapCache = items.stream()
                    .collect(Collectors.toMap(AreaCodeItem::getAreaCode, AreaCodeItem::getAreaName));
        }
        return areaCodeNameMapCache;
    }

    private final Map<String, Map<String, String>> cat3NameCache = new HashMap<>();

    private String getCat3Name(String cat1, String cat2, String cat3) {
        if (cat1 == null || cat2 == null || cat3 == null) return "알 수 없음";

        String cacheKey = cat1 + "-" + cat2;
        Map<String, String> map = cat3NameCache.get(cacheKey);

        if (map == null) {
            map = touristSpotApiService.getCat3NameMap(cat1, cat2);
            cat3NameCache.put(cacheKey, map);
        }

        return map.getOrDefault(cat3, "알 수 없음");
    }

    private final Map<String, Map<String, String>> cat2NameCache = new HashMap<>();

    private String getCat2Name(String cat1, String cat2) {
        if (cat1 == null || cat2 == null) return "알 수 없음";

        Map<String, String> map = cat2NameCache.get(cat1);

        if (map == null) {
            map = touristSpotApiService.getCat2NameMap(cat1);
            cat2NameCache.put(cat1, map);
        }

        return map.getOrDefault(cat2, "알 수 없음");
    }


    //관광지 상세조회
    public TouristSpotDetailResponse getTouristSpotDetail(Long id, int pageNo, int numOfRows) {
        TouristSpot spot = touristSpotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 관광지가 없습니다. id=" + id));

        // detailCommon2
        DetailTouristSpotItem detail = touristSpotApiService.getTouristSpotDetail(
                String.valueOf(spot.getContentid()), pageNo, numOfRows);

        // detailIntro2 추가
        DetailTouristSpotItem intro = touristSpotApiService.getTouristSpotIntro(
                String.valueOf(spot.getContentid()), pageNo, numOfRows);

        // 병합: detail 안에 intro 내용 덮어씌우기
        detail.setInfocenter(intro.getInfocenter());
        detail.setRestdate(intro.getRestdate());
        detail.setUsetime(intro.getUsetime());
        detail.setParking(intro.getParking());
        detail.setChkbabycarriage(intro.getChkbabycarriage());
        detail.setChkpet(intro.getChkpet());
        detail.setChkcreditcard(intro.getChkcreditcard());

        return TouristSpotDetailResponse.from(spot, detail);
    }

    //관광지 상세조회 기반 주변 관광지 정보  raduis 1km = 1000 (till 20000)
    public List<NearbyItem> getNearbyTouristSpots(Long id, int radius, int contentTypeId) {
        TouristSpot spot = touristSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 관광지를 찾을 수 없습니다."));

        List<NearbyItem> rawNearbySpots = nearbyTouristSpotApiService.getNearbySpots(
                spot.getMapx(),
                spot.getMapy(),
                radius,
                contentTypeId
        );

        return rawNearbySpots.stream()
                // 자기 자신 제외 (mapx, mapy 비교 혹은 id 비교)
                .filter(item -> {
                    // DB에서 mapx, mapy 기준으로 TouristSpot 찾아서 id 비교
                    TouristSpot matched = touristSpotRepository
                            .findByMapxAndMapy(item.getMapx(), item.getMapy())
                            .orElse(null);
                    return matched == null || !matched.getId().equals(id);
                })
                .map(item -> {
                    TouristSpot matched = touristSpotRepository
                            .findByMapxAndMapy(item.getMapx(), item.getMapy())
                            .orElse(null);

                    return NearbyItem.builder()
                            .id(matched != null ? matched.getId() : null)
                            .title(item.getTitle())
                            .mapx(item.getMapx())
                            .mapy(item.getMapy())
                            .firstimage(item.getFirstimage())
                            .tel(item.getTel())
                            .addr1(item.getAddr1())
                            .build();
                })
                .toList();
    }


    //거리 계산 유틸 메서드
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (단위: km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    //사용자 위치 기반 반경 이내 50곳 확인
    public List<TouristSpotListResponse> getNearbyTouristSpotsFromCoords(double userMapx, double userMapy, int radiusKm) {
        List<TouristSpot> allSpots = touristSpotRepository.findAll();

        return allSpots.stream()
                .map(spot -> {
                    try {
                        double dist = haversine(
                                userMapy, userMapx,
                                Double.parseDouble(spot.getMapy()),
                                Double.parseDouble(spot.getMapx())
                        );
                        return new Object() {
                            TouristSpot s = spot;
                            double distance = dist;
                        };
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(obj -> obj != null && obj.distance <= radiusKm)
                .sorted((a, b) -> Double.compare(a.distance, b.distance)) // 가까운 순 정렬
                .limit(50) // 최대 50개만
                .map(obj -> {
                    TouristSpot spot = obj.s;
                    return TouristSpotListResponse.builder()
                            .id(spot.getId())
                            .title(spot.getTitle())
                            .addr1(spot.getAddr1())
                            .firstimage(spot.getFirstimage())
                            .mapx(spot.getMapx())
                            .mapy(spot.getMapy())
                            .build();
                })
                .toList();
    }


}
