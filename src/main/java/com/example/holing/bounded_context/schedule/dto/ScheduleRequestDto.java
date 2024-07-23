package com.example.holing.bounded_context.schedule.dto;

import com.example.holing.bounded_context.schedule.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleRequestDto(
        String title,
        String content,
        LocalDateTime startAt,
        LocalDateTime finishAt
) {
    public Schedule toEntity() {
        return Schedule.builder()
                .title(title)
                .content(content)
                .startAt(startAt)
                .finishAt(finishAt)
                .build();
    }
}