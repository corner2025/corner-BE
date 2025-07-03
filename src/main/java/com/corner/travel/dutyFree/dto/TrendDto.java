// src/main/java/com/corner/travel/dutyfree/dto/TrendDto.java
package com.corner.travel.dutyfree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrendDto {
    private String yearMonth;
    private Long totalSales;
}
//이것도 추이 그래프때 쓸수도