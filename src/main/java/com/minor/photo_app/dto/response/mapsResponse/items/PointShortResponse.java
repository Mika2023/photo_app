package com.minor.photo_app.dto.response.mapsResponse.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointShortResponse {
    private Double lat;
    private Double lon;
}
