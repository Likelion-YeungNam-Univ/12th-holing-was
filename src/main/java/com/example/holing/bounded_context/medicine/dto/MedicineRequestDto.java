package com.example.holing.bounded_context.medicine.dto;

import com.example.holing.bounded_context.medicine.entity.Medicine;

import java.time.LocalTime;

public record MedicineRequestDto(
        String name,
        LocalTime takenAt
) {
    public static Medicine toEntity(MedicineRequestDto medicineRequestDto) {
        return Medicine.builder()
                .name(medicineRequestDto.name)
                .takenAt(medicineRequestDto.takenAt)
                .build();
    }
}
