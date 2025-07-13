package com.corner.travel.touristSpot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TouristSpotItem {
    private String addr1;
    private String addr2;
    private String areacode;
    private String cat1;
    private String cat2;
    private String cat3;
    @JsonProperty("contentId")
    private String contentid;
    @JsonProperty("contentTypeId")
    private String contenttypeid;
    private String createdtime;
    private String firstimage;
    private String firstimage2;
    private String cpyrhtDivCd;
    private String mapx;
    private String mapy;
    private String mlevel;
    private String modifiedtime;
    private String sigungucode;
    private String tel;
    private String title;
    private String zipcode;
    @JsonProperty("lDongRegnCd")
    private String lDongRegnCd;
    @JsonProperty("lDongSignguCd")
    private String lDongSignguCd;
    private String lclsSystm1;
    private String lclsSystm2;
    private String lclsSystm3;
}
