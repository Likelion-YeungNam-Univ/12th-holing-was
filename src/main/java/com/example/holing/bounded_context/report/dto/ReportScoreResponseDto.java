package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.UserReport;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

public record ReportScoreResponseDto(
        int month,
        int weekOfMonth,
        List<ReportScoreDto> reportList
) {
    public static ReportScoreResponseDto fromEntity(UserReport userReport) {
        LocalDateTime createdAt = userReport.getCreatedAt();
        return new ReportScoreResponseDto(
                createdAt.getMonthValue(),
                createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                userReport.getReports().stream()
                        .map(ReportScoreDto::fromEntity).toList()
        );
    }
}
