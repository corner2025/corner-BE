package com.corner.travel.festival.service;

import com.corner.travel.festival.dto.FestivalDto;

import java.util.List;
import java.util.Optional;

public interface FestivalService {
    List<FestivalDto> getFestivalList(int pageNo, int numOfRows);
    Optional<FestivalDto> getFestivalDetail(String festivalName);
}
