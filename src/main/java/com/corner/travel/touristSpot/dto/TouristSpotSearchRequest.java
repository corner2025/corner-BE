package com.corner.travel.touristSpot.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TouristSpotSearchRequest {
    // private String areaCode;
    // private String sigunguCode;
    private String keyword;
    private String areaName;
}
