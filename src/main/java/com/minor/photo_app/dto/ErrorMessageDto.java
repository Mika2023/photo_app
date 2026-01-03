package com.minor.photo_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageDto {
    private String message;
    private Instant timestamp;
    private Integer statusCode;
    private String error;
    private Map<String, String> fieldErrors;
}
