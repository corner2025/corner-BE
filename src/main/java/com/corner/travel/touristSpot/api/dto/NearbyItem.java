package com.corner.travel.touristSpot.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyItem {
    private String title;
    private String mapx;
    private String mapy;
    private String firstimage;
    private String tel;
    private String addr1;
}


