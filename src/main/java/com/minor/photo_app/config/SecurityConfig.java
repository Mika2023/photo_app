package com.minor.photo_app.config;

import com.minor.photo_app.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm ->
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**",
                                "/v3/api-docs/**", "/api-docs/**","/auth/**",
                                "/monitoring", "/", "/*.*", "/assets/**", "/icons/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public RouterFunction<ServerResponse> spaRouter() {
        return RouterFunctions.route(
                RequestPredicates.GET("/**")
                        .and(path("/api/**").negate())
                        .and(path("/swagger-ui/**").negate())
                        .and(path("/v3/api-docs/**").negate())
                        .and(path("/actuator/**").negate())
                        .and(request -> {
                            String path = request.path();
                            return !path.contains(".");
                        })
                        .and(request -> {
                            String accept = request.headers().firstHeader("Accept");
                            return accept != null && accept.contains("text/html");
                        }),

                request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(new ClassPathResource("static/index.html"))
        );
    }
}
