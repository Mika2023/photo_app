package com.minor.photo_app.dto.filters;

import com.minor.photo_app.enums.PlaceSort;
import com.minor.photo_app.enums.TransportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceFilter {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Set<Long> categoryIds;
    private Map<TransportType, Set<String>> selectedStops;
    private WorkingHoursFilter workingHours;
    private Boolean isFavoriteByUser;

    private PlaceSort sort;
    private Integer page;
    private Integer size;
}
