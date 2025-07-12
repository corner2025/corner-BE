package com.corner.travel.festival.service;

import com.corner.travel.festival.dto.FestivalDto;
import java.util.List;
import java.util.Optional;

public interface FestivalService {
    /**
     * 외부 API에서 페스티벌 목록 조회
     * @param pageNo 1-base 페이지 번호
     * @param numOfRows 페이지 크기
     */
    List<FestivalDto> getFestivalList(int pageNo, int numOfRows);

    /**
     * ID 기반 축제 상세 조회
     * @param festivalId DB PK
     */
    Optional<FestivalDto> getFestivalDetail(Long festivalId);

    /**
     * DB에 저장된 페스티벌을 페이징 조회
     * @param pageNo 1-base 페이지 번호
     * @param pageSize 페이지 크기
     */
    List<FestivalDto> findAllFromDb(int pageNo, int pageSize);
}
