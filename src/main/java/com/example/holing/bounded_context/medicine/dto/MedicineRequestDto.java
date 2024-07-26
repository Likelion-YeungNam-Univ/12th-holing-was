package com.example.holing.bounded_context.medicine.dto;

import com.example.holing.bounded_context.medicine.entity.Medicine;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record MedicineRequestDto(
        @Schema(description = "영양제 이름", example = "탁센")
        String name,
        @Schema(description = "영양제 복용 시간", example = "16:00")
        LocalTime takenAt
) {
    public static Medicine toEntity(MedicineRequestDto medicineRequestDto) {
        return Medicine.builder()
                .name(medicineRequestDto.name)
                .takenAt(medicineRequestDto.takenAt)
                .build();
    }
}
