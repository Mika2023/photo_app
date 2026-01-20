package com.minor.photo_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private Long id;
    private String name;
    private Instant createdAt;
    private List<CoordinateResponse> path;
}
