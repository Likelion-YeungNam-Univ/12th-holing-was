package com.example.holing.bounded_context.mission.dto;

import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.survey.entity.Tag;

public record MissionResponseDto(
        String content,
        Tag tag,
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
