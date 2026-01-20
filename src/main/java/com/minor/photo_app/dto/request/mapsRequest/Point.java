package com.minor.photo_app.dto.request.mapsRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    private Double lon;
    private Double lat;
    private String type = "stop";
}
