package com.minor.photo_app.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {
    private String baseUrl;
    private String apiKey;
    private String bucketName;
}
