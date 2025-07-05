package com.corner.travel.performance.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;


import java.util.List;

@Data
@XmlRootElement(name = "dbs")
@XmlAccessorType(XmlAccessType.FIELD)
public class PerformanceApiListResponse {
    @XmlElement(name = "db")
    private List<PerformanceApiDto> performance;
}
