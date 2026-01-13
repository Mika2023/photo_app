package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationRequest {
    @NotNull
    @Digits(integer = 3, fraction = 6)
    private Double latitude;

    @NotNull
    @Digits(integer = 3, fraction = 6)
    private Double longitude;
}
