package com.minor.photo_app.repository;

import com.minor.photo_app.entity.SystemEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemEventRepository extends JpaRepository<SystemEvent,Long> {

    Long countSystemEventsByEventName(String eventName);
}
