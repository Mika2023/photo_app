package com.minor.photo_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlacePhotoResponse {
    private Long id;
    private Long placeId;
    private String userName;
    private String imageUrl;
    private Instant createdAt;
    private Boolean is_main;
}
