package com.minor.photo_app.repository;

import com.minor.photo_app.entity.Place;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>, JpaSpecificationExecutor<Place> {
    List<Place> findAllByNameContainingIgnoreCase(@Param("name") String name);

    @Query(value = """
        SELECT p.*
        FROM places p
        WHERE p.id <> :placeId
          AND ST_DWithin(
                p.location::geometry,
                ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geometry,
                :radiusMeters
          )
        LIMIT :limit
        """,
            nativeQuery = true)
    List<Place> findPlacesNearby(@Param("placeId") Long placeId,
                                 @Param("lat") double lat,
                                 @Param("lon") double lon,
                                 @Param("radiusMeters") double radiusMeters,
                                 @Param("limit") int limit);

    @Query(value = "SELECT location FROM places WHERE id = :id", nativeQuery = true)
    Optional<Point> findLocationById(@Param("id") Long id);
}
