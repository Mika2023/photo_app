package com.minor.photo_app.dto.response.mapsResponse.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapsPlaceResponse {
    private String id;
    private String name;
    @JsonProperty("full_address_name")
    private String fullAddressName;
    private PointShortResponse point;
    @JsonProperty("schedule")
    private Map<String, Object> schedule;
    private LinksToPlaceResponse links;
    private List<RubricsResponse> rubrics;
}
