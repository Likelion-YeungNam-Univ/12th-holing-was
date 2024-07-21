package com.example.holing.bounded_context.missionresult.dto;

import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.missionresult.entity.MissionResult;
import com.example.holing.bounded_context.missionresult.entity.MissionState;

public record MissionResultResponseDto(
        Long id,
        MissionState state,
        Mission mission
) {
    public static MissionResultResponseDto fromEntity(MissionResult missionResult) {
        return new MissionResultResponseDto(
                missionResult.getId(),
                missionResult.getState(),
                missionResult.getMission()
        );
    }
}
