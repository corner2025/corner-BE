// src/main/java/com/corner/travel/dutyfree/repository/projection/MonthlySales.java
package com.corner.travel.dutyfree.repository.projection;

public interface MonthlySales {
    String getYearMonth();
    Long getTotalSales();
}
