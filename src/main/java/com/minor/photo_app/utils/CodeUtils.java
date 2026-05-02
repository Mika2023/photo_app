package com.minor.photo_app.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class CodeUtils {

    public static String getRandomSixthCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
