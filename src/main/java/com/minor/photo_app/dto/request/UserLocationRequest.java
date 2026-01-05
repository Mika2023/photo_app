package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationRequest {
    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
