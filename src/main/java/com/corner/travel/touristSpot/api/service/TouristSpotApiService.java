package com.corner.travel.touristSpot.api.service;

import com.corner.travel.touristSpot.api.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TouristSpotApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${tourist.service-key}")
    private String serviceKey;

    public List<?> getTouristSpots(int pageNo, int numOfRows, String areaCode, String sigunguCode, String keyword) {
        try {
            // ✅ 원본 키를 인코딩 (URL 안전하게)
            //String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

            // 디버깅용 출력
            System.out.println("원본 서비스키: " + serviceKey);
            //System.out.println("인코딩된 서비스키: " + encodedServiceKey);

            // ✅ URL 빌드
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService2/areaBasedList2");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC");
            urlBuilder.append("&MobileApp=AppTest");
            urlBuilder.append("&contentTypeId=12");
            urlBuilder.append("&arrange=C");
            urlBuilder.append("&numOfRows=").append(numOfRows);
            urlBuilder.append("&pageNo=").append(pageNo);

            if (areaCode != null && !areaCode.isEmpty()) {
                urlBuilder.append("&areaCode=").append(areaCode);
            }
            if (sigunguCode != null && !sigunguCode.isEmpty()) {
                urlBuilder.append("&sigunguCode=").append(sigunguCode);
            }

            urlBuilder.append("&_type=json");

            String url = urlBuilder.toString();

            // ✅ 헤더 설정 (중요!)
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            headers.set("Accept", "application/json");
            // headers.set("Content-Type", "application/json; charset=UTF-8");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // ✅ 헤더와 함께 요청
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String rawResponse = response.getBody();

            System.out.println("요청 URL: " + url);
            System.out.println("원본 응답 문자열: " + rawResponse);

            // ✅ JSON 응답 아닐 경우 (ex: 인증 실패 XML), 예외 처리
            if (rawResponse != null && rawResponse.startsWith("<")) {
                throw new RuntimeException("API 호출 실패 - XML 응답: " + rawResponse);
            }

            // ✅ JSON 파싱
            ApiResponse apiResponse = objectMapper.readValue(rawResponse, ApiResponse.class);

            if (apiResponse.getResponse() == null ||
                    apiResponse.getResponse().getBody() == null ||
                    apiResponse.getResponse().getBody().getItems() == null) {
                return List.of(); // 빈 리스트
            }

            return apiResponse.getResponse().getBody().getItems().getItem();

        } catch (Exception e) {
            throw new RuntimeException("관광지 데이터 요청 또는 파싱 실패: " + e.getMessage(), e);
        }
    }
}