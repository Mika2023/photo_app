package com.minor.photo_app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SystemEventNames {
    VISITED_MAIN_SCREEN("visited_main_screen"),
    VISITED_ROUTES_SCREEN("visited_routes_screen");

    private final String eventName;

    @JsonCreator
    public static SystemEventNames fromValue(String value) {
        return Arrays.stream(values())
                .filter(t -> t.eventName.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Неизвестный тип события: " + value)
                );
    }

    @JsonValue
    public String toJson() {
        return eventName;
    }
}
