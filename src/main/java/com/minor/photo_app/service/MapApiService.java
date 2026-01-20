package com.minor.photo_app.service;

import com.minor.photo_app.dto.response.mapsResponse.MapsApiResponse;
import com.minor.photo_app.enums.TransportTypeForMapApi;

public interface MapApiService {
    MapsApiResponse buildRoute(Double fromLat,
                               Double fromLon,
                               Double toLat,
                               Double toLon,
                               TransportTypeForMapApi transportType);
}
