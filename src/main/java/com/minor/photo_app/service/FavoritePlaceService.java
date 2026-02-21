package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.filters.PlaceFilter;
import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.entity.FavoritePlace;
import com.minor.photo_app.entity.FavoritePlaceId;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.repository.FavoritePlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritePlaceService {
    private final FavoritePlaceRepository favoritePlaceRepository;
    private final UserService userService;
    private final PlaceService placeService;

    @Transactional(readOnly = true)
    public Set<Long> getFavoritePlaceIdsByUser(UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);

        Set<FavoritePlace> favoritePlaces = favoritePlaceRepository.findAllByUser(user);
        if (favoritePlaces == null || favoritePlaces.isEmpty()) {
            return Set.of();
        }

        return favoritePlaces.stream()
                .map(f -> f.getId().getPlaceId())
                .collect(Collectors.toSet());
    }

    public Slice<PlaceCardResponse> getFavoritePlacesByUser(UserPrincipal userPrincipal) {
        PlaceFilter placeFilter = new PlaceFilter();
        placeFilter.setIsFavoriteByUser(true);
        return placeService.findPlacesByFilter(userPrincipal, placeFilter);
    }

    @Transactional
    public void createOrDeleteFavoritePlace(UserPrincipal userPrincipal, Long placeId) {
        User user = userService.getUserByPrincipal(userPrincipal);

        Long  userId = user.getId();
        if (favoritePlaceRepository.existsByPlaceIdAndUserId(placeId, userId)) {
            favoritePlaceRepository.deleteByPlaceIdAndUserId(placeId, userId);
        }
        else {
            FavoritePlaceId favoritePlaceId = new FavoritePlaceId();
            favoritePlaceId.setPlaceId(placeId);
            favoritePlaceId.setUserId(userId);

            FavoritePlace favoritePlace = new FavoritePlace();
            favoritePlace.setUser(user);
            Place place = placeService.getPlace(placeId);
            favoritePlace.setPlace(place);
            favoritePlace.setId(favoritePlaceId);
            favoritePlaceRepository.save(favoritePlace);
        }
    }
}
