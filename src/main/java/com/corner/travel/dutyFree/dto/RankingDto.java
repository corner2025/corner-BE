// src/main/java/com/corner/travel/dutyfree/dto/RankingDto.java
package com.corner.travel.dutyfree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingDto {
    private String category;
    private Long totalSales;
}
