package com.minor.photo_app.customConverter;

import com.minor.photo_app.enums.SystemEventNames;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SystemEventNamesConverter implements Converter<String, SystemEventNames> {
    @Override
    public SystemEventNames convert(String systemEventName) {
        return SystemEventNames.fromValue(systemEventName);
    }
}
