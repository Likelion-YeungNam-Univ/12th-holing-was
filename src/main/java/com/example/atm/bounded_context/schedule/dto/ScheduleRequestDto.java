package com.example.atm.bounded_context.schedule.dto;

import com.example.atm.bounded_context.schedule.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleRequestDto(
        String title,
        String place,
        LocalDateTime startAt,
        LocalDateTime finishAt
) {
    public Schedule toEntity() {
        return Schedule.builder()
                .title(title)
                .place(place)
                .startAt(startAt)
                .finishAt(finishAt)
                .build();
    }
}