package com.corner.travel.festival.service;

import com.corner.travel.festival.domain.Festival;
import com.corner.travel.festival.dto.FestivalDto;
import com.corner.travel.festival.repository.FestivalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FestivalRepository repo;

    @Value("${data.festival.api.url}")
    private String apiUrl;

    @Value("${data.festival.api.key}")
    private String serviceKey;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public List<FestivalDto> getFestivalList(int pageNo, int numOfRows) {
        String uriStr = String.format(
                "%s?serviceKey=%s&MobileOS=WEB&MobileApp=WebTest" +
                        "&pageNo=%d&numOfRows=%d&eventStartDate=20250701&_type=json",
                apiUrl, serviceKey, pageNo, numOfRows
        );
        URI uri = URI.create(uriStr);
        System.out.println("▶▶▶ External API URI = " + uriStr);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> resp = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String body = resp.getBody() != null ? resp.getBody().trim() : "";

        if (body.startsWith("<")) {
            throw new RuntimeException("External API Error: " + body);
        }

        List<FestivalDto> result = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode items = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            if (items.isArray()) {
                for (JsonNode node : items) {
                    result.add(parseFestival(node));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Festival JSON 파싱 실패", e);
        }

        List<Festival> entities = result.stream()
                .map(dto -> {
                    Festival f = new Festival();
                    f.setTitle(dto.getTitle());
                    f.setAddr1(dto.getAddr1());
                    f.setAddr2(dto.getAddr2());
                    f.setEventStartDate(LocalDate.parse(dto.getEventStartDate(), FMT));
                    f.setEventEndDate(LocalDate.parse(dto.getEventEndDate(), FMT));
                    f.setFirstImage(dto.getFirstImage());
                    f.setFirstImage2(dto.getFirstImage2());
                    f.setMapX(Double.valueOf(dto.getMapX()));
                    f.setMapY(Double.valueOf(dto.getMapY()));
                    f.setTel(dto.getTel());
                    return f;
                })
                .collect(Collectors.toList());
        repo.deleteAll();
        repo.saveAll(entities);

        return result;
    }

    private FestivalDto parseFestival(JsonNode node) {
        return new FestivalDto(
                null,
                node.path("title").asText(""),
                node.path("addr1").asText(""),
                node.path("addr2").asText(""),
                node.path("eventstartdate").asText(""),
                node.path("eventenddate").asText(""),
                node.path("firstimage").asText(""),
                node.path("firstimage2").asText(""),
                node.path("mapx").asText(""),
                node.path("mapy").asText(""),
                node.path("tel").asText("")
        );
    }

    @Override
    public Optional<FestivalDto> getFestivalDetail(Long festivalId) {
        return repo.findById(festivalId)
                .map(entity -> new FestivalDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getAddr1(),
                        entity.getAddr2(),
                        entity.getEventStartDate().format(FMT),
                        entity.getEventEndDate().format(FMT),
                        entity.getFirstImage(),
                        entity.getFirstImage2(),
                        String.valueOf(entity.getMapX()),
                        String.valueOf(entity.getMapY()),
                        entity.getTel()
                ));
    }

    @Override
    public List<FestivalDto> findAllFromDb(int pageNo, int pageSize, String startDate, String endDate, String location, String title) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Festival> page = repo.findByFilters(startDate, endDate, location, title, pageable);

        return page.stream()
                .map(entity -> new FestivalDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getAddr1(),
                        entity.getAddr2(),
                        entity.getEventStartDate().format(FMT),
                        entity.getEventEndDate().format(FMT),
                        entity.getFirstImage(),
                        entity.getFirstImage2(),
                        String.valueOf(entity.getMapX()),
                        String.valueOf(entity.getMapY()),
                        entity.getTel()
                ))
                .collect(Collectors.toList());
    }

}
