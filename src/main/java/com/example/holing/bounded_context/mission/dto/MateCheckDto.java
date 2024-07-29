package com.example.holing.bounded_context.mission.dto;

public record MateCheckDto(
        boolean isMateConnected
) {
    public static MateCheckDto fromEntity(boolean state) {
        return new MateCheckDto(
                state
        );
    }
}
