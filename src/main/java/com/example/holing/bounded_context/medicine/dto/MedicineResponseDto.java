package com.example.holing.bounded_context.medicine.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Time;
import java.time.LocalTime;

public record MedicineResponseDto(
        @Schema(description = "영양제 아이디", example = "1")
        Long id,
        @Schema(description = "영양제 이름", example = "탁센")
        String name,
        @Schema(description = "영양제 복용 시간", example = "16:00")
        LocalTime takenAt,
        @Schema(description = "복용 유무", example = "true")
        Boolean isTaken
) {
    public static MedicineResponseDto fromEntity(Object[] objects) {
        return new MedicineResponseDto(
                (Long) objects[0],
                (String) objects[1],
                ((Time) objects[2]).toLocalTime(),
                (Long) objects[4] != null
        );
    }
}
