package com.minor.photo_app.dto.recommendation;

import com.minor.photo_app.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class PlaceCardWithScoreDto {
    private Place place;
    private double score;
    private double distanceMeters;
    private double finalScore;
}
