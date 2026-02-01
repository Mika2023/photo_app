package com.minor.photo_app.config.webclient;

import com.minor.photo_app.properties.DoubleGisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class DGisWebClientConfig {

    private final DoubleGisProperties properties;
    private final WebClient.Builder webClientBuilder;

    @Bean
    public WebClient dGisWebClient() {
        return webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
