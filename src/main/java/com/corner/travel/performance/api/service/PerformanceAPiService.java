package com.corner.travel.performance.api.service;


import com.corner.travel.performance.api.dto.PerformanceApiListResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;

@Service
@RequiredArgsConstructor
public class PerformanceAPiService {

    private final RestTemplate restTemplate;

    private static final String SERVICE_KEY = "9af065f1ddeb4ed298b8aee9c196e386";

    public String fetchPerformanceList(String startDate, String endDate, int page, int rows) {
        String url = "http://www.kopis.or.kr/openApi/restful/pblprfr"
                + "?service=" + SERVICE_KEY
                + "&stdate=" + startDate
                + "&eddate=" + endDate
                + "&cpage=" + page
                + "&rows=" + rows;

        return restTemplate.getForObject(url, String.class);
    }
    // 새로 추가: XML 파싱 후 DTO 반환
    public PerformanceApiListResponse fetchAndParsePerformanceList(String startDate, String endDate, int page, int rows) {
        String xml = fetchPerformanceList(startDate, endDate, page, rows);

        try {
            JAXBContext context = JAXBContext.newInstance(PerformanceApiListResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (PerformanceApiListResponse) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            throw new RuntimeException("KOPIS 공연 리스트 XML 파싱 실패", e);
        }
    }

}
