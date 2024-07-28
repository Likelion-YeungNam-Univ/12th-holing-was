package com.example.holing.bounded_context.medicine.dto;

import com.example.holing.bounded_context.medicine.entity.Medicine;
import com.example.holing.bounded_context.medicine.entity.MedicineHistory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public record MedicineResponseDto(
        Long id,
        String name,
        LocalTime takenAt,
        Boolean isTaken
) {
    public static MedicineResponseDto fromEntity(Medicine medicine) {
        Optional<MedicineHistory> recentHistory = medicine.getMedicineHistoryList().stream().findFirst();

        return new MedicineResponseDto(
                medicine.getId(),
                medicine.getName(),
                medicine.getTakenAt(),
                recentHistory.isPresent() &&
                        LocalDate.now().equals(recentHistory.get().getCreatedAt().toLocalDate())
        );
    }
}
