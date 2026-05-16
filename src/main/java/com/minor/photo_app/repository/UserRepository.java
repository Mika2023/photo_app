package com.minor.photo_app.repository;

import com.minor.photo_app.dto.recommendation.FeatureCountProjection;
import com.minor.photo_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByNickname(@Param("nickname") String nickname);

    Boolean existsByEmail(@Param("email") String email);

    Optional<User> findByNickname(@Param("nickname") String nickname);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);

    @Query("SELECT c.id AS featureId, COUNT(p.id) AS featureCount " +
            "FROM User u " +
            "JOIN u.favoritePlaces p " +
            "JOIN p.categories c " +
            "WHERE u.id = :userId " +
            "GROUP BY c.id")
    List<FeatureCountProjection> countFavoriteCategories(@Param("userId") Long userId);

    @Query("SELECT t.id AS featureId, COUNT(p.id) AS featureCount " +
            "FROM User u " +
            "JOIN u.favoritePlaces p " +
            "JOIN p.tags t " +
            "WHERE u.id = :userId " +
            "GROUP BY t.id")
    List<FeatureCountProjection> countFavoriteTags(@Param("userId") Long userId);

    @Query("SELECT c.id AS featureId, COUNT(r.id) AS featureCount " +
            "FROM Route r " +
            "JOIN r.toPlace p " +
            "JOIN p.categories c " +
            "WHERE r.user.id = :userId " +
            "GROUP BY c.id")
    List<FeatureCountProjection> countRouteCategories(@Param("userId") Long userId);

    @Query("SELECT t.id AS featureId, COUNT(r.id) AS featureCount " +
            "FROM Route r " +
            "JOIN r.toPlace p " +
            "JOIN p.tags t " +
            "WHERE r.user.id = :userId " +
            "GROUP BY t.id")
    List<FeatureCountProjection> countRouteTags(@Param("userId") Long userId);

    @Query("SELECT p.id FROM User u JOIN u.favoritePlaces p WHERE u.id = :userId " +
            "UNION " +
            "SELECT r.toPlace.id FROM Route r WHERE r.user.id = :userId")
    Set<Long> getKnownPlaces(@Param("userId") Long userId);
}
