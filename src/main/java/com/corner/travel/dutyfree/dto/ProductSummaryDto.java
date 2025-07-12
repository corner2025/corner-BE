// src/main/java/com/corner/travel/dutyfree/dto/ProductSummaryDto.java
package com.corner.travel.dutyfree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSummaryDto {
    private Long id;
    private String yearMonth;
    private String category;
    private Integer salesCount;
}
//월별 추이 그래프 만들때 사용할수도 .,