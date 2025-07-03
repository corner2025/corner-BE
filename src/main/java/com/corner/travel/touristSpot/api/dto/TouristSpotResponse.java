package com.corner.travel.touristSpot.api.dto;

import lombok.Data;

@Data
public class TouristSpotResponse {
    private TouristSpotHeader header;
    private TouristSpotBody body;
}
