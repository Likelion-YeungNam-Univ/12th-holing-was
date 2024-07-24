package com.example.holing.bounded_context.schedule.dto;

import java.time.LocalDate;

public record ScheduleCountDto(
        LocalDate date,
        int count
) {
    public ScheduleCountDto scheduleCountDto(LocalDate date, int count) {
        return new ScheduleCountDto(date, count);
    }
}
