package com.corner.travel.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "data.festival.api")
public class FestivalApiProperties {
    /**
     * application.yml 에서
     * data.festival.api.url
     */
    private String url;

    /**
     * application.yml 에서
     * data.festival.api.key
     */
    private String key;
}
