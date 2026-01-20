package com.minor.photo_app.dto.request;

import com.minor.photo_app.enums.TransportTypeForMapApi;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {

    @NotNull
    private Long toPlaceId;

    private String name;
    private String description;
    private Long fromPlaceId;
    private TransportTypeForMapApi transportType;
}
