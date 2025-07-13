package com.corner.travel.festival.service;

import com.corner.travel.festival.dto.FestivalDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FestivalServiceImpl implements FestivalService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${data.festival.api.url}")
    private String apiUrl;

    // yml에 디코딩된 키(+, =) 그대로 넣어두고 사용
    @Value("${data.festival.api.key}")
    private String serviceKey;



    public FestivalServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<FestivalDto> getFestivalList(int pageNo, int numOfRows) {
        String uriStr = String.format("%s?serviceKey=%s&pageNo=%d&numOfRows=%d&type=json",
                apiUrl, serviceKey, pageNo, numOfRows);

        System.out.println("▶▶▶ External API URI = " + uriStr);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // URI 객체 생성 시 인코딩 안 함
        URI uri = URI.create(uriStr);

        ResponseEntity<String> resp = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                String.class
        );

        System.out.println("▶▶▶ External API status = " + resp.getStatusCodeValue());
        System.out.println("▶▶▶ External API body   = " + resp.getBody());

        String body = resp.getBody() != null ? resp.getBody().trim() : "";

        // XML 에러 감지
        if (body.startsWith("<")) {
            String code = body.replaceAll("(?s).*<resultCode>(\\d+)</resultCode>.*", "$1");
            String msg  = body.replaceAll("(?s).*<resultMsg>([^<]+)</resultMsg>.*", "$1");
            if ("30".equals(code)) {
                throw new RuntimeException("축제 API 서비스 준비 중입니다. (키 승인 대기: " + msg + ")");
            }
            throw new RuntimeException("External API Error: code=" + code + ", msg=" + msg);
        }

        List<FestivalDto> result = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(body);
            JsonNode itemsNode = rootNode
                    .path("response")
                    .path("body")
                    .path("items");  // .path("item") 제거!

            System.out.println("▶▶▶ Items node exists: " + !itemsNode.isMissingNode());
            System.out.println("▶▶▶ Items node type: " + itemsNode.getNodeType());

            if (itemsNode.isArray()) {
                for (JsonNode node : itemsNode) {
                    FestivalDto festival = parseFestival(node);
                    result.add(festival);
                }
            } else if (itemsNode.isObject()) {
                System.out.println("▶▶▶ Items is single object");
                result.add(parseFestival(itemsNode));
            } else {
                System.out.println("▶▶▶ Items node is missing or null");
            }

            System.out.println("▶▶▶ Final result size: " + result.size());

        } catch (Exception e) {
            System.out.println("▶▶▶ JSON 파싱 에러: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Festival JSON 파싱 실패", e);
        }

        return result;
    }


    private FestivalDto parseFestival(JsonNode node) {
        return new FestivalDto(
                node.path("fstvlNm").asText(""),
                node.path("opar").asText(""),
                node.path("fstvlStartDate").asText(""),
                node.path("fstvlEndDate").asText(""),
                node.path("mnnstNm").asText(""),
                node.path("auspcInsttNm").asText(""),
                node.path("phoneNumber").asText(""),
                node.path("homepageUrl").asText(""),
                node.path("rdnmadr").asText(""),
                node.path("lnmadr").asText(""),
                node.path("latitude").asText(""),
                node.path("longitude").asText("")
        );
    }

    @Override
    public Optional<FestivalDto> getFestivalDetail(String festivalName) {
        return getFestivalList(1, 1000).stream()
                .filter(f -> f.getFstvlNm().equals(festivalName))
                .findFirst();
    }
}
