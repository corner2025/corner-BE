// src/main/java/com/corner/travel/festival/dto/FestivalDto.java
package com.corner.travel.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FestivalDto {
    private Long id;
    private String title;
    private String addr1;
    private String addr2;
    private String eventStartDate;
    private String eventEndDate;
    private String firstImage;
    private String firstImage2;
    private String mapX;
    private String mapY;
    private String tel;
}
