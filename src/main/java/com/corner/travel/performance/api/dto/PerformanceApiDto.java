package com.corner.travel.performance.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;


@Data
@XmlAccessorType(XmlAccessType.FIELD)

public class PerformanceApiDto {
    @XmlElement(name = "mt20id")
    private String id;

    @XmlElement(name = "prfnm")
    private String name;

    @XmlElement(name = "prfpdfrom")
    private String startDate;

    @XmlElement(name = "prfpdto")
    private String endDate;

    @XmlElement(name = "fcltynm")
    private String place;

    @XmlElement(name = "poster")
    private String posterUrl;

    @XmlElement(name = "area")
    private String area;

    @XmlElement(name = "genrenm")
    private String genre;

    @XmlElement(name = "openrun")
    private String openRun;

    @XmlElement(name = "prfstate")
    private String state;
}
