package com.minor.photo_app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TransportTypeForMapApi {
    WALKING("walking", List.of("pedestrian_instructions")),
    CAR("driving", null),
    BICYCLE("bicycle", List.of("pedestrian_instructions"));

    private final String value;
    private final List<String> options;

    @JsonCreator
    public static TransportTypeForMapApi fromValue(String value) {
        return Arrays.stream(values())
                .filter(t -> t.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Неизвестный тип передвижения: " + value)
                );
    }

    @JsonValue
    public String toJson() {
        return value;
    }
}
