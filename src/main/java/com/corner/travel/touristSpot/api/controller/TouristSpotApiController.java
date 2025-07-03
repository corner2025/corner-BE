package com.corner.travel.touristSpot.api.controller;

import com.corner.travel.touristSpot.api.service.TouristSpotApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tourist-spot")
public class TouristSpotApiController {

    private final TouristSpotApiService touristSpotService;

    @GetMapping("/test")
    public List<?> testTouristSpots(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows,
            @RequestParam(required = false) String areaCode,
            @RequestParam(required = false) String sigunguCode,
            @RequestParam(required = false) String keyword
    ) {
        int pNo = (pageNo == null) ? 1 : pageNo;
        int nRows = (numOfRows == null) ? 10 : numOfRows;
        return touristSpotService.getTouristSpots(pNo, nRows, areaCode, sigunguCode, keyword);
    }


}
