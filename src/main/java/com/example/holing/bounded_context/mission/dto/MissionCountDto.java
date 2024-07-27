package com.example.holing.bounded_context.mission.dto;

import java.time.LocalDate;

public record MissionCountDto(
        LocalDate date,
        int count
) {
    public static MissionCountDto missionCountDto(LocalDate date, int count) {
        return new MissionCountDto(date, count);
    }
}
