package com.minor.photo_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCardResponse {
    private Long id;
    private String name;
    private Boolean isFavorite;
    private String mainImageUrl;
}
