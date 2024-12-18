package com.example.holing.bounded_context.mission.dto;

import com.example.holing.bounded_context.mission.entity.MissionResult;

public record MissionResultResponseDto(
        Long id,
        boolean isCompleted,
        MissionInfoDto missionInfoDto
) {
    public static MissionResultResponseDto fromEntity(MissionResult missionResult) {
        return new MissionResultResponseDto(
                missionResult.getId(),
                missionResult.isCompleted(),
                MissionInfoDto.fromEntity(missionResult.getMission())
        );
    }
}
