package com.minor.photo_app.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "gis.maps")
public class DoubleGisProperties {
    private String baseUrl;
    private String apiKey;
}
