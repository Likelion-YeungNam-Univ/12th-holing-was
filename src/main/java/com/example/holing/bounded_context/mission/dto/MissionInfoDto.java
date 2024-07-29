package com.example.holing.bounded_context.mission.dto;

import com.example.holing.bounded_context.mission.entity.Mission;

public record MissionInfoDto(
        Long id,
        String missionTitle,
        String missionContent,
        String tagName,

        int reward
) {
    public static MissionInfoDto fromEntity(Mission mission) {
        return new MissionInfoDto(
                mission.getId(),
                mission.getTitle(),
                mission.getContent(),
                mission.getTag().getName(),
                mission.getReward()
        );
    }
}
