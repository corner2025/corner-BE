package com.corner.travel.touristSpot.api.dto;

import lombok.Data;

@Data
public class TouristSpotBody {
    private TouristSpotItems items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
