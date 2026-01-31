package com.minor.photo_app.dto.response.mapsResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultResponse {
    @JsonProperty("begin_pedestrian_path")
    private PedestrianPath beginPedestrianPath;

    @JsonProperty("end_pedestrian_path")
    private PedestrianPath endPedestrianPath;

    @JsonProperty("total_distance")
    private Integer totalDistance;

    @JsonProperty("total_duration")
    private Integer totalDuration;

    private List<Maneuver> maneuvers;
}
