package com.minor.photo_app.repository;

import com.minor.photo_app.entity.RoutePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePlaceRepository extends JpaRepository<RoutePlace,Long> {
}
