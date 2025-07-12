package com.corner.travel.touristSpot.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyBody {
    private NearbyItems items;
}
