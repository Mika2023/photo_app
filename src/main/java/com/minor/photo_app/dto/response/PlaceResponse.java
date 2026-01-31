package com.minor.photo_app.dto.response;

import com.minor.photo_app.dto.PointDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private PointDto location;
    private Map<String, List<String>> locationDescription;
    private Map<String, List<Map<String, String>>> workingHours;
    private BigDecimal visitCost;
    private Set<TagResponse> tags;
    private Set<PlacePhotoResponse> photos;
    private Set<CategoryShortInfoResponse> categories;
    private Instant createdAt;
    private Boolean isFavorite;
}
