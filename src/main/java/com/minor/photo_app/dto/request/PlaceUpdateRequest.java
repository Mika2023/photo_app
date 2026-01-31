package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceUpdateRequest {
    private String name;
    private String description;
    private String address;
    private Map<String, List<String>> locationDescription;
    private Map<String, List<Map<String, String>>>  workingHours;

    @Digits(integer = 3, fraction = 6)
    private Double latitude;

    @Digits(integer = 3, fraction = 6)
    private Double longitude;

    @Min(value = 0)
    private BigDecimal visitCost;

    private List<Long> categoryIds;
}
