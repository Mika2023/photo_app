package com.minor.photo_app.repository;

import com.minor.photo_app.entity.Route;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    Slice<Route> findAllByUserId(Long userId, PageRequest pageRequest);
}
