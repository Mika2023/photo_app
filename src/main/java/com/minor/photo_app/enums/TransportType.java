package com.minor.photo_app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TransportType {
    METRO("Метро"),
    BUS("Автобус"),
    TRAM("Трамвай"),
    TRAIN("Поезд"),
    SUBURBAN_TRAIN("Пригородный поезд");

    private final String type;

    @JsonCreator
    public static TransportType fromValue(String value) {
        return Arrays.stream(values())
                .filter(t -> t.type.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Неизвестный тип транспорта: " + value)
                );
    }

    @JsonValue
    public String toJson() {
        return type;
    }

    public static String getRussianNameByType(String enType) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(enType))
                .findFirst()
                .map(TransportType::getType)
                .orElse(null);
    }
}
