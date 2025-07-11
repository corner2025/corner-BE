package com.corner.travel.festival.dto;
import lombok.Data;

@Data
public class FestivalDto {
    private String fstvlNm;        // 축제명
    private String opar;           // 개최장소
    private String fstvlStartDate; // 시작일자 (YYYYMMDD)
    private String fstvlEndDate;   // 종료일자 (YYYYMMDD)
    private String mnnstNm;        // 주관기관명
    private String auspcInsttNm;   // 주최기관명
    private String phoneNumber;    // 연락처
    private String homepageUrl;    // 홈페이지 URL
    private String rdnmadr;        // 도로명주소
    private String lnmadr;         // 지번주소
    private String latitude;       // 위도
    private String longitude;      // 경도

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
