package com.minor.photo_app.controller;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.CreateEventRequest;
import com.minor.photo_app.dto.response.SystemEventCountResponse;
import com.minor.photo_app.dto.response.SystemEventForUserResponse;
import com.minor.photo_app.enums.SystemEventNames;
import com.minor.photo_app.service.SystemEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("system-events")
@RequiredArgsConstructor
public class SystemEventController {

    private final SystemEventService systemEventService;

    @PostMapping
    public void createEvent(@RequestBody @Valid CreateEventRequest createEventRequest,
                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        systemEventService.createEvent(createEventRequest, userPrincipal);
    }

    @GetMapping("/get-events-for-user")
    public Set<SystemEventForUserResponse> getEventForUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return systemEventService.getEventsForUser(userPrincipal);
    }

    @GetMapping("/count-event-by-name")
    public SystemEventCountResponse countEventByName(@RequestParam SystemEventNames eventName) {
        return systemEventService.countEventByName(eventName);
    }
}
