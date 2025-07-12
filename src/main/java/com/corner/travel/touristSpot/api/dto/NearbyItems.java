package com.corner.travel.touristSpot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NearbyItems {
    @JsonProperty
    private List<NearbyItem> item;
}
