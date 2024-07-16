package com.example.atm.bounded_context.schedule.dto;

import com.example.atm.bounded_context.schedule.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponseDto(
        String title,
        String place,
        LocalDateTime startAt,
        LocalDateTime finishAt
) {
    public static ScheduleResponseDto fromEntity(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getTitle(),
                schedule.getPlace(),
                schedule.getStartAt(),
                schedule.getFinishAt()
        );
    }
}
