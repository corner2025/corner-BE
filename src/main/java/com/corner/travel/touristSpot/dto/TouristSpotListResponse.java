package com.corner.travel.touristSpot.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class TouristSpotListResponse {

    private Long id;
    private String title;
    private String addr1;
    private String firstimage;
    private String mapx;
    private String mapy;
}
