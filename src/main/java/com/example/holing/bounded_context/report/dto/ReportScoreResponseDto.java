package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.UserReport;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

public record ReportScoreResponseDto(
        @Schema(description = "월", example = "7")
        int month,
        @Schema(description = "주차", example = "1")
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
