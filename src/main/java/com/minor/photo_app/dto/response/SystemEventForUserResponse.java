package com.minor.photo_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemEventForUserResponse {

    private Long id;
    private String eventName;
    private String userId;
    private Instant createdDate;
}
