package com.minor.photo_app.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtils {

    public static Long parseLongOrNull(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
