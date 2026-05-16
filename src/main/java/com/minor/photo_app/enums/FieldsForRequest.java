package com.minor.photo_app.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum FieldsForRequest {
    POINT,
    FULL_ADDRESS_NAME,
    SCHEDULE,
    LINKS,
    RUBRICS;

    public static String getItemsRequest() {
        return Arrays.stream(FieldsForRequest.values())
                .map(fieldForRequest ->
                        StringUtils.join("items.", fieldForRequest.name().toLowerCase()))
                .collect(Collectors.joining(","));
    }
}
