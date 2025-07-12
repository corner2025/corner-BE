// src/main/java/com/corner/travel/festival/domain/Festival.java
package com.corner.travel.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "festival")
@Getter
@Setter
@NoArgsConstructor
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String addr1;
    private String addr2;

    @Column(name = "event_start_date")
    private LocalDate eventStartDate;

    @Column(name = "event_end_date")
    private LocalDate eventEndDate;

    @Column(columnDefinition = "TEXT")
    private String firstImage;

    @Column(columnDefinition = "TEXT")
    private String firstImage2;

    private Double mapX;
    private Double mapY;

    private String tel;
}
