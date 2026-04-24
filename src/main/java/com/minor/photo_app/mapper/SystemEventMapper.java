package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.request.CreateEventRequest;
import com.minor.photo_app.dto.response.SystemEventForUserResponse;
import com.minor.photo_app.entity.SystemEvent;
import com.minor.photo_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SystemEventMapper {

    @Mapping(target = "eventName", source = "request.eventName.eventName")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    SystemEvent toEntity(CreateEventRequest request, User user);

    @Mapping(target = "userId", source = "user.id")
    SystemEventForUserResponse toUserResponse(SystemEvent systemEvent);
    Set<SystemEventForUserResponse> toUserResponse(Set<SystemEvent> systemEvents);
}
