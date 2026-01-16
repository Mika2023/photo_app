package com.minor.photo_app.repository;

import com.minor.photo_app.entity.FavoritePlace;
import com.minor.photo_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, Long> {
    Set<FavoritePlace> findAllByUser(User user);

    Boolean existsByPlaceIdAndUserId(@Param("placeId") Long placeId,  @Param("userId") Long userId);

    void deleteByPlaceIdAndUserId(@Param("placeId") Long placeId, @Param("userId") Long userId);

}
