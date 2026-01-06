package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.entity.FavoritePlace;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.repository.FavoritePlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritePlaceService {
    private final FavoritePlaceRepository favoritePlaceRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Set<Long> getFavoritePlacesByUser(UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);

        Set<FavoritePlace> favoritePlaces = favoritePlaceRepository.findAllByUser(user);
        if (favoritePlaces == null || favoritePlaces.isEmpty()) {
            throw new NotFoundException("У пользователя нет избранных мест");
        }

        return favoritePlaces.stream()
                .map(f -> f.getId().getPlaceId())
                .collect(Collectors.toSet());
    }
}
