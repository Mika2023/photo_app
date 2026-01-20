package com.minor.photo_app.dto.request.mapsRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class MapApiRequest {
    private String locale = "ru";
    private String transport;
    private List<String> options;
    private List<String> filters = List.of();
    private List<Point> points;
    private Long utc = Instant.now().getEpochSecond();
}
