package com.corner.travel.festival.dto;

import lombok.Data;

@Data
public class FestivalDto {
    private String fstvlNm;
    private String opar;
    private String fstvlStartDate;
    private String fstvlEndDate;
    private String mnnstNm;
    private String auspcInsttNm;
    private String phoneNumber;
    private String homepageUrl;
    private String rdnmadr;
    private String lnmadr;
    private String latitude;
    private String longitude;

    // 전체 필드 생성자
    public FestivalDto(String fstvlNm, String opar, String fstvlStartDate, String fstvlEndDate,
                       String mnnstNm, String auspcInsttNm, String phoneNumber,
                       String homepageUrl, String rdnmadr, String lnmadr,
                       String latitude, String longitude) {
        this.fstvlNm = fstvlNm;
        this.opar = opar;
        this.fstvlStartDate = fstvlStartDate;
        this.fstvlEndDate = fstvlEndDate;
        this.mnnstNm = mnnstNm;
        this.auspcInsttNm = auspcInsttNm;
        this.phoneNumber = phoneNumber;
        this.homepageUrl = homepageUrl;
        this.rdnmadr = rdnmadr;
        this.lnmadr = lnmadr;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
