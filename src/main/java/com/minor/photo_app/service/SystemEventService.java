package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.CreateEventRequest;
import com.minor.photo_app.dto.response.SystemEventCountResponse;
import com.minor.photo_app.dto.response.SystemEventForUserResponse;
import com.minor.photo_app.entity.SystemEvent;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.enums.SystemEventNames;
import com.minor.photo_app.mapper.SystemEventMapper;
import com.minor.photo_app.repository.SystemEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemEventService {

    private final SystemEventRepository systemEventRepository;
    private final SystemEventMapper systemEventMapper;
    private final UserService userService;

    public void createEvent(CreateEventRequest createEventRequest, UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);

        SystemEvent systemEvent = systemEventMapper.toEntity(createEventRequest, user);
        systemEventRepository.save(systemEvent);
    }

    public Set<SystemEventForUserResponse> getEventsForUser(UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);

        return systemEventMapper.toUserResponse(user.getSystemEvents());
    }

    public SystemEventCountResponse countEventByName(SystemEventNames eventName) {
        String eventNameString = eventName.getEventName();
        Long countOfEvents = systemEventRepository.countSystemEventsByEventName(eventNameString);

        return new SystemEventCountResponse()
                .setCountOfEvent(countOfEvents)
                .setEventName(eventNameString);
    }
}
