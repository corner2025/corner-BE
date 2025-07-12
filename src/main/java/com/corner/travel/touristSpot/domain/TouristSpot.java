package com.corner.travel.touristSpot.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tourist_spot")
public class TouristSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String addr1;
    private String addr2;
    private String areacode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String contentid;
    private String contenttypeid;
    private String firstimage;
    private String firstimage2;
    private String mapx;
    private String mapy;
    private String sigungucode;
    private String tel;
    private String zipcode;

    // 필요한 필드만 우선 반영
}

