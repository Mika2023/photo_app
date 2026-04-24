package com.minor.photo_app.dto.request;

import com.minor.photo_app.enums.SystemEventNames;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    @NotNull
    private SystemEventNames eventName;
}
