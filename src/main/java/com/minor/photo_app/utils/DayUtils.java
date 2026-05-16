package com.minor.photo_app.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.util.Arrays;

@UtilityClass
public class DayUtils {
    public static boolean isDayOfWeek(String dayOfWeek) {
        if (StringUtils.isBlank(dayOfWeek)) return false;

        return Arrays.stream(DayOfWeek.values())
                .anyMatch(d -> d.name().toLowerCase().startsWith(dayOfWeek.toLowerCase()));
    }
}
