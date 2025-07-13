package com.corner.travel.touristSpot.dto;

import com.corner.travel.touristSpot.api.dto.DetailTouristSpotItem;
import com.corner.travel.touristSpot.domain.TouristSpot;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TouristSpotDetailResponse {
    private Long id;
    private String title;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String tel;
    private String mapx;
    private String mapy;
    private String firstimage;
    private String firstimage2;

    private String overview;
    private String homepage;

    private String infocenter;
    private String restdate;
    private String usetime;
    private String parking;
    private String chkbabycarriage;
    private String chkpet;
    private String chkcreditcard;



//    public static TouristSpotDetailResponse from(TouristSpot entity) {
//        return TouristSpotDetailResponse.builder()
//                .id(entity.getId())
//                .title(entity.getTitle())
//                .addr1(entity.getAddr1())
//                .addr2(entity.getAddr2())
//                .zipcode(entity.getZipcode())
//                .tel(entity.getTel())
//                .mapx(entity.getMapx())
//                .mapy(entity.getMapy())
//                .firstimage(entity.getFirstimage())
//                .firstimage2(entity.getFirstimage2())
//                .build();
//    }

    public static TouristSpotDetailResponse from(TouristSpot entity, DetailTouristSpotItem detail) {
        return TouristSpotDetailResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .addr1(entity.getAddr1())
                .addr2(entity.getAddr2())
                .zipcode(entity.getZipcode())
                .tel(entity.getTel())
                .mapx(entity.getMapx())
                .mapy(entity.getMapy())
                .firstimage(entity.getFirstimage())
                .firstimage2(entity.getFirstimage2())
                .overview(detail.getOverview())
                .homepage(detail.getHomepage())
                .infocenter(detail.getInfocenter())
                .restdate(detail.getRestdate())
                .usetime(detail.getUsetime())
                .parking(detail.getParking())
                .chkbabycarriage(detail.getChkbabycarriage())
                .chkpet(detail.getChkpet())
                .chkcreditcard(detail.getChkcreditcard())
                .build();
    }
}
