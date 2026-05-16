package com.minor.photo_app.service.mapApi;

import com.minor.photo_app.dto.request.mapsRequest.MapApiRequest;
import com.minor.photo_app.dto.request.mapsRequest.Point;
import com.minor.photo_app.dto.response.mapsResponse.MapsApiResponse;
import com.minor.photo_app.dto.response.mapsResponse.items.MapsItemsResponse;
import com.minor.photo_app.enums.FieldsForRequest;
import com.minor.photo_app.enums.TransportTypeForMapApi;
import com.minor.photo_app.exception.MapApiException;
import com.minor.photo_app.properties.DoubleGisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoubleGisApiService implements MapApiService {

    private final static String MOSCOW_ID = "4504222397630173";

    private final DoubleGisProperties properties;
    private final WebClient dGisWebClient;
    private final WebClient itemsDGisWebClient;

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

    @Override
    public MapsItemsResponse getPlaces(Set<String> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            throw new IllegalArgumentException("Передан пустой список категорий");
        }
        String rubricIdLine = String.join(",", categoryIds);
        String itemsRequest = FieldsForRequest.getItemsRequest();

        return itemsDGisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/3.0/items")
                        .queryParam("rubric_id", "{rubricIdLine}")
                        .queryParam("city_id", MOSCOW_ID)
                        .queryParam("fields", "{itemsRequest}")
                        .queryParam("page_size", 10)
                        .queryParam("key", properties.getApiKey())
                        .build(Map.of(
                                "rubricIdLine", rubricIdLine,
                                "itemsRequest", itemsRequest
                        ))
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new MapApiException(
                                        "Не удалось выполнить запрос к 2ГИС, ошибка - " + clientResponse.statusCode(),
                                        clientResponse.statusCode()
                                )
                        )
                )
                .bodyToMono(MapsItemsResponse.class)
                .onErrorResume(e ->
                        Mono.error(new MapApiException("Не удалось выполнить запрос к 2ГИС, ошибка - " + e))
                )
                .block(Duration.ofSeconds(15));
    }
}
