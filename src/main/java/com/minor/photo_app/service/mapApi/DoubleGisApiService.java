package com.minor.photo_app.service.mapApi;

import com.minor.photo_app.dto.request.mapsRequest.MapApiRequest;
import com.minor.photo_app.dto.request.mapsRequest.Point;
import com.minor.photo_app.dto.response.mapsResponse.MapsApiResponse;
import com.minor.photo_app.enums.TransportTypeForMapApi;
import com.minor.photo_app.exception.MapApiException;
import com.minor.photo_app.properties.DoubleGisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoubleGisApiService implements MapApiService {

    private final DoubleGisProperties properties;
    private final WebClient dGisWebClient;

    @Override
    public MapsApiResponse buildRoute(
            Double fromLat,
            Double fromLon,
            Double toLat,
            Double toLon,
            TransportTypeForMapApi transportType) {
        Point fromPoint = new Point();
        fromPoint.setLat(fromLat);
        fromPoint.setLon(fromLon);

        Point toPoint = new Point();
        toPoint.setLat(toLat);
        toPoint.setLon(toLon);

        MapApiRequest request = new MapApiRequest()
                .setTransport(transportType.getValue())
                .setOptions(transportType.getOptions())
                .setPoints(List.of(fromPoint, toPoint));

        return dGisWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/7.0.0/global")
                        .queryParam("key", properties.getApiKey())
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new MapApiException(
                                "Не удалось выполнить запрос к 2ГИС, ошибка - " + clientResponse.statusCode(),
                                clientResponse.statusCode()
                                )
                        )
                )
                .bodyToMono(MapsApiResponse.class)
                .onErrorResume(e ->
                    Mono.error(new MapApiException("Не удалось выполнить запрос к 2ГИС, ошибка - " + e.getMessage()))
                )
                .block();
    }
}
