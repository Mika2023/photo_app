package com.minor.photo_app.dto.response.mapsResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultResponse {
    private PedestrianPath beginPedestrianPath;
    private PedestrianPath endPedestrianPath;
    private Integer totalDistance;
    private Integer totalDuration;
    private List<Maneuver> maneuvers;
}
