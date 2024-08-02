package com.example.holing.bounded_context.medicine.dto;

import java.sql.Time;
import java.time.LocalTime;

public record MedicineResponseDto(
        Long id,
        String name,
        LocalTime takenAt,
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
