package com.minor.photo_app.config.webclient;

import com.minor.photo_app.properties.SupabaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class SupabaseWebClientConfig {
    private final SupabaseProperties supabaseProperties;
    private final WebClient.Builder webClientBuilder;

    @Bean
    public WebClient supabaseWebClient() {
        return webClientBuilder
                .baseUrl(supabaseProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION,  "Bearer " + supabaseProperties.getApiKey())
                .defaultHeader("apikey", supabaseProperties.getApiKey())
                .build();
    }
}
