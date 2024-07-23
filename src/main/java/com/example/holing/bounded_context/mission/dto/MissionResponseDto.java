package com.example.holing.bounded_context.mission.dto;

import com.example.holing.bounded_context.mission.entity.Mission;

public record MissionResponseDto(
        String content,
        String tag,
        int reward
) {
    public static MissionResponseDto fromEntity(Mission mission) {
        return new MissionResponseDto(
                mission.getContent(),
                mission.getTag(),
                mission.getReward()
        );
    }
}
