package com.minor.photo_app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PlaceSort {
    NEWEST("Новизна"),
    PRICE_ASC("Возрастание цены"),
    PRICE_DESC("Убывание цены"),
    POPULARITY("Популярность"),
    CLOSEST("Близость"),
    DEFAULT("По умолчанию");

    private final String name;

    @JsonCreator
    public static PlaceSort fromValue(String value) {
        return Arrays.stream(values())
                .filter(t -> t.name.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Неизвестный тип сортировки: " + value)
                );
    }

    @JsonValue
    public String toJson() {
        return name;
    }
}
