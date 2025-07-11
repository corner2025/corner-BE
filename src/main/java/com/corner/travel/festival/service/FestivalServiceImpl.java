package com.corner.travel.festival.service;

import com.corner.travel.festival.dto.FestivalDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        // 1) URL을 직접 조합 (serviceKey는 yml에 인코딩된 형태로 들어있음)
        String uriStr = String.format("%s?serviceKey=%s&pageNo=%d&numOfRows=%d&type=json",
                apiUrl,
                serviceKey,
                pageNo,
                numOfRows
        );
        System.out.println("▶▶▶ External API URI    = " + uriStr);

        // 2) API 호출
        ResponseEntity<String> resp = restTemplate.getForEntity(uriStr, String.class);
        System.out.println("▶▶▶ External API status = " + resp.getStatusCodeValue());
        System.out.println("▶▶▶ External API body   = " + resp.getBody());

        String body = resp.getBody() != null ? resp.getBody().trim() : "";

        // 3) XML 에러 감지 (resultCode != "00")
        if (body.startsWith("<")) {
            String code = body.replaceAll("(?s).*<resultCode>(\\d+)</resultCode>.*", "$1");
            String msg  = body.replaceAll("(?s).*<resultMsg>([^<]+)</resultMsg>.*", "$1");
            if ("30".equals(code)) {
                throw new RuntimeException("축제 API 서비스 준비 중입니다. (키 승인 대기: " + msg + ")");
            }
            throw new RuntimeException("External API Error: code=" + code + ", msg=" + msg);
        }

        // 4) JSON 파싱 (이전과 동일)
        List<FestivalDto> result = new ArrayList<>();
        try {
            JsonNode items = objectMapper.readTree(body)
                    .path("response")
                    .path("body")
                    .path("items");    // items가 바로 ArrayNode

            if (items.isArray()) {
                for (JsonNode node : items) {
                    result.add(new FestivalDto(
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
                    ));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Festival JSON 파싱 실패", e);
        }

        return result;
    }


    @Override
    public Optional<FestivalDto> getFestivalDetail(String festivalName) {
        return getFestivalList(1, 1000).stream()
                .filter(f -> f.getFstvlNm().equals(festivalName))
                .findFirst();
    }
}
