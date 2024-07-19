package com.example.holing.bounded_context.schedule.dto;

import com.example.holing.bounded_context.schedule.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponseDto(
        Long id,
        String title,
        String place,
        LocalDateTime startAt,
        LocalDateTime finishAt
) {
    public static ScheduleResponseDto fromEntity(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getPlace(),
                schedule.getStartAt(),
                schedule.getFinishAt()
        );
    }
}
