package com.minor.photo_app.dto.response.mapsResponse.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHoursResponse {
    @JsonProperty("working_hours")
    private List<PeriodResponse> workingHours;
}
