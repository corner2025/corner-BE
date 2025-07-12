package com.corner.travel.dutyfree.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "duty_free_product")
public class DutyFreeProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year_month", nullable = false, length = 7)
    private String yearMonth;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "sales_count", nullable = false)
    private Integer salesCount;

    // 기본 생성자
    protected DutyFreeProduct() {}

    public DutyFreeProduct(String yearMonth, String category, Integer salesCount) {
        this.category   = category;
        this.salesCount = salesCount;
        this.yearMonth = yearMonth;
    }

    // getters only (불변 객체 스타일)
    public Long   getId()         { return id; }
    public String getYearMonth()  { return yearMonth; }
    public String getCategory()   { return category; }
    public Integer getSalesCount(){ return salesCount; }
}