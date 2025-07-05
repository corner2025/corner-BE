package com.corner.travel.performance.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "performance")
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Performance {
    @Id
    @Column(length = 20)
    private String id;          // mt20id

    @Column(length = 200, nullable = false)
    private String name;        // prfnm

    @Column(length = 20)
    private String startDate;   // prfpdfrom

    @Column(length = 20)
    private String endDate;     // prfpdto

    @Column(length = 200)
    private String place;       // fcltynm

    @Column(length = 500)
    private String posterUrl;   // poster

    @Column(length = 100)
    private String area;        // area

    @Column(length = 100)
    private String genre;       // genrenm

    @Column(length = 5)
    private String openRun;     // openrun

    @Column(length = 20)
    private String state;       // prfstate
}
