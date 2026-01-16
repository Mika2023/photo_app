package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCreateRequest {
    private Boolean isMain;

    @NotNull
    private Long placeId;
}
