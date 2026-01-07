package com.minor.photo_app.dto.filters;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Data
public class WorkingHoursFilter {
    private Set<DayOfWeek> openDays;
    private LocalTime from;
    private LocalTime to;
}
