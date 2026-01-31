package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCreationRequest {

    @NotNull
    private String name;
    private String description;
    private String address;
    private Map<String, List<String>> locationDescription;
    private Map<String, List<Map<String, String>>> workingHours;

    @NotNull
    @Min(value = 0)
    private BigDecimal visitCost;

    @NotNull
    @Digits(integer = 3, fraction = 6)
    private Double latitude;

    @NotNull
    @Digits(integer = 3, fraction = 6)
    private Double longitude;

    @NotEmpty
    private List<Long> categoryIds;
}
