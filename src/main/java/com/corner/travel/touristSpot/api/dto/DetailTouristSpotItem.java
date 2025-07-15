package com.corner.travel.touristSpot.api.dto;

import lombok.Data;

@Data
public class DetailTouristSpotItem {
    private String overview;     // 개요 (상세 설명)
    private String homepage;     // 홈페이지 주소

    private String infocenter;
    private String restdate;
    private String usetime;
    private String parking;
    private String chkbabycarriage;
    private String chkpet;
    private String chkcreditcard;
}
