package com.corner.travel.touristSpot.api.service;

import com.corner.travel.touristSpot.api.dto.NearbyApiResponse;
import com.corner.travel.touristSpot.api.dto.NearbyItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NearbyTouristSpotApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${tourist.service-key}")
    private String serviceKey;

    public List<NearbyItem> getNearbySpots(String mapx, String mapy, int radius, int contentTypeId) {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService2/locationBasedList2");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC&MobileApp=AppTest");
            urlBuilder.append("&mapX=").append(mapx);
            urlBuilder.append("&mapY=").append(mapy);
            urlBuilder.append("&radius=").append(radius);
            urlBuilder.append("&contentTypeId=").append(contentTypeId);
            urlBuilder.append("&_type=json");

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");

            ResponseEntity<String> response = restTemplate.exchange(
                    urlBuilder.toString(), HttpMethod.GET, new HttpEntity<>(headers), String.class
            );

            String body = response.getBody();

            if (body != null && body.startsWith("<")) {
                throw new RuntimeException("응답이 XML입니다. 인증 문제일 수 있습니다.");
            }

            NearbyApiResponse apiResponse = objectMapper.readValue(body, NearbyApiResponse.class);
            return apiResponse.getResponse().getBody().getItems().getItem();
        } catch (Exception e) {
            throw new RuntimeException("주변 관광지 조회 실패: " + e.getMessage(), e);
        }
    }
}
