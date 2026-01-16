package com.minor.photo_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponse {
    private Long id;
    private String imageUrl;
    private Instant createdAt;
    private PlaceShortResponse place;
}
