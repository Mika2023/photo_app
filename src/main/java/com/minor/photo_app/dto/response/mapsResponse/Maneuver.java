package com.minor.photo_app.dto.response.mapsResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Maneuver {
    private String comment;

    @JsonProperty("outcoming_path")
    private OutcomingPath outcomingPath;

    @JsonProperty("outcoming_path_comment")
    private String outcomingPathComment;
}
