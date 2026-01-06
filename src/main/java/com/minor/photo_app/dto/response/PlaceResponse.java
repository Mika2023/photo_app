package com.minor.photo_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponse {
    private Long id;
    private String name;
    private String description;
    private Point location;
    private String locationDescription;
    private String workingHours;
    private BigDecimal visitCost;
    private Set<TagResponse> tags;
    private Set<PlacePhotoResponse> photos;
    private Instant createdAt;
}
