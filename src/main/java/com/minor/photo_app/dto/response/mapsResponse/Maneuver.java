package com.minor.photo_app.dto.response.mapsResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Maneuver {
    private String comment;
    private OutcomingPath outcomingPath;
    private String outcomingPathComment;
}
