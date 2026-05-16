package com.minor.photo_app.scheduler;

import com.minor.photo_app.dto.response.mapsResponse.items.MapsItemsResponse;
import com.minor.photo_app.dto.response.mapsResponse.items.MapsPlaceResponse;
import com.minor.photo_app.exception.PhotoAppException;
import com.minor.photo_app.service.CategoryService;
import com.minor.photo_app.service.PlaceService;
import com.minor.photo_app.service.mapApi.MapApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class TwoGisSyncScheduler {

    private final MapApiService mapApiService;
    private final CategoryService categoryService;
    private final PlaceService placeService;

    @Scheduled(cron = "${scheduler.sync-cron}")
    public void syncPlacesFromMaps() {
        log.info("Начало синхронизации мест с картами");

        Set<String> categoryIds = categoryService.getCategoryTwoGisIds();
        MapsItemsResponse itemsResponse = mapApiService.getPlaces(categoryIds);

        if (Objects.isNull(itemsResponse) ||
                Objects.isNull(itemsResponse.getResult()) ||
                CollectionUtils.isEmpty(itemsResponse.getResult().getItems())) {
            log.error("Ошибка синхронизации - ответ от апи карт пришел пустой");
            throw new PhotoAppException(String.format("Ответ от апи карт пришел пустой на запрос %s", categoryIds));
        }

        List<MapsPlaceResponse> placesFromMaps = itemsResponse.getResult().getItems();
        placeService.syncPlacesFromMaps(placesFromMaps);
        log.info("Конец синхронизации мест с картами");
    }
}
