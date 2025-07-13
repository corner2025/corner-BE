package com.corner.travel.touristSpot.dto;


import lombok.Data;

@Data
public class NearbyTouristSpotDto {
    private Long id;
    private String title;
    private String mapx;
    private String mapy;
    private String firstimage;
    private String tel;
}
