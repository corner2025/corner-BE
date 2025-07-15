package com.corner.travel.touristSpot.api.service;

import com.corner.travel.touristSpot.api.dto.ApiResponse;
import com.corner.travel.touristSpot.api.dto.AreaCodeItem;
import com.corner.travel.touristSpot.api.dto.DetailTouristSpotItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TouristSpotApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String contentTypeId = "12";

    @Value("${tourist.service-key}")
    private String serviceKey;

    //전체조회
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
    // 상세조회
    public DetailTouristSpotItem getTouristSpotDetail(String contentId, int pageNo, int numOfRows) {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService2/detailCommon2");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC");
            urlBuilder.append("&MobileApp=AppTest");
            urlBuilder.append("&contentId=").append(contentId);
            urlBuilder.append("&numOfRows=").append(numOfRows);
            urlBuilder.append("&pageNo=").append(pageNo);
            urlBuilder.append("&_type=json");

            String url = urlBuilder.toString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            String rawResponse = response.getBody();
            System.out.println("요청 URL: " + url);
            System.out.println("원본 응답 문자열: " + rawResponse);

            if (rawResponse != null && rawResponse.startsWith("<")) {
                throw new RuntimeException("API 호출 실패 - XML 응답: " + rawResponse);
            }

            JsonNode root = objectMapper.readTree(rawResponse);
            JsonNode item = root.path("response").path("body").path("items").path("item");

            DetailTouristSpotItem detail = new DetailTouristSpotItem();
            if (item.isArray() && item.size() > 0) {
                JsonNode data = item.get(0);
                detail.setOverview(data.path("overview").asText(""));
                detail.setHomepage(data.path("homepage").asText(""));
            } else if (item.isObject()) {
                detail.setOverview(item.path("overview").asText(""));
                detail.setHomepage(item.path("homepage").asText(""));
            }

            return detail;

        } catch (Exception e) {
            throw new RuntimeException("관광지 상세 정보 요청 또는 파싱 실패: " + e.getMessage(), e);
        }
    }

    public DetailTouristSpotItem getTouristSpotIntro(String contentId, int pageNo, int numOfRows) {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService2/detailIntro2");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC");
            urlBuilder.append("&MobileApp=AppTest");
            urlBuilder.append("&contentTypeId=12");
            urlBuilder.append("&contentId=").append(contentId);
            urlBuilder.append("&numOfRows=").append(numOfRows);
            urlBuilder.append("&pageNo=").append(pageNo);
            urlBuilder.append("&_type=json");

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, entity, String.class);

            String rawResponse = response.getBody();
            if (rawResponse != null && rawResponse.startsWith("<")) {
                throw new RuntimeException("API 호출 실패 - XML 응답: " + rawResponse);
            }

            JsonNode root = objectMapper.readTree(rawResponse);
            JsonNode item = root.path("response").path("body").path("items").path("item");

            if ((item.isArray() && item.size() > 0)) item = item.get(0);

            DetailTouristSpotItem intro = new DetailTouristSpotItem();
            intro.setInfocenter(item.path("infocenter").asText(""));
            intro.setRestdate(item.path("restdate").asText(""));
            intro.setUsetime(item.path("usetime").asText(""));
            intro.setParking(item.path("parking").asText(""));
            intro.setChkbabycarriage(item.path("chkbabycarriage").asText(""));
            intro.setChkpet(item.path("chkpet").asText(""));
            intro.setChkcreditcard(item.path("chkcreditcard").asText(""));

            return intro;

        } catch (Exception e) {
            throw new RuntimeException("detailIntro2 호출 실패: " + e.getMessage(), e);
        }
    }

    public List<AreaCodeItem> getAreaCodeList() {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService2/areaCode2");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC");
            urlBuilder.append("&MobileApp=AppTest");
            urlBuilder.append("&_type=json");
            urlBuilder.append("&numOfRows=100"); // 넉넉하게

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, entity, String.class);

            String body = response.getBody();
            JsonNode root = objectMapper.readTree(body);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            List<AreaCodeItem> result = new ArrayList<>();
            if (items.isArray()) {
                for (JsonNode item : items) {
                    result.add(new AreaCodeItem(
                            item.path("code").asText(),  // areacode
                            item.path("name").asText()   // 지역명
                    ));
                }
            }
            return result;

        } catch (Exception e) {
            throw new RuntimeException("지역 코드 API 호출 실패: " + e.getMessage(), e);
        }
    }

    private final Map<String, Map<String, String>> cat3NameCache = new HashMap<>();

    public Map<String, String> getCat3NameMap(String cat1, String cat2) {
        String cacheKey = cat1 + "-" + cat2;
        if (cat3NameCache.containsKey(cacheKey)) return cat3NameCache.get(cacheKey);

        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService2/categoryCode2");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC&MobileApp=AppTest");
            urlBuilder.append("&contentTypeId=12");
            urlBuilder.append("&cat1=").append(cat1);
            urlBuilder.append("&cat2=").append(cat2);
            urlBuilder.append("&numOfRows=100&pageNo=1&_type=json");

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");

            ResponseEntity<String> response = restTemplate.exchange(
                    urlBuilder.toString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

            JsonNode items = objectMapper.readTree(response.getBody())
                    .path("response").path("body").path("items").path("item");

            Map<String, String> map = new HashMap<>();
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String code = item.path("code").asText();
                    String name = item.path("name").asText();
                    map.put(code, name);
                }
            }
            cat3NameCache.put(cacheKey, map);
            return map;

        } catch (Exception e) {
            throw new RuntimeException("cat3Name 조회 실패: " + e.getMessage(), e);
        }
    }

    public Map<String, String> getCat2NameMap(String cat1) {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService2/categoryCode2");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC");
            urlBuilder.append("&MobileApp=AppTest");
            urlBuilder.append("&contentTypeId=12");
            urlBuilder.append("&cat1=").append(cat1);
            urlBuilder.append("&_type=json");
            urlBuilder.append("&pageNo=1&numOfRows=100");

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, entity, String.class);

            String raw = response.getBody();
            JsonNode root = objectMapper.readTree(raw);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            Map<String, String> result = new HashMap<>();
            if (items.isArray()) {
                for (JsonNode item : items) {
                    result.put(item.path("code").asText(), item.path("name").asText());
                }
            } else if (items.isObject()) {
                result.put(items.path("code").asText(), items.path("name").asText());
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("cat2Name API 호출 실패: " + e.getMessage(), e);
        }
    }
}