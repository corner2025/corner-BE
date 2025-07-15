package com.corner.travel.touristSpot.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyItem {
    private Long id;
    private String title;
    private String mapx;
    private String mapy;
    private String firstimage;
    private String tel;
    private String addr1;
}


