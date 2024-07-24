package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.UserReport;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public record ReportDetailResponseDto(
        int month,
        int weekOfMonth,
        List<ReportDetailDto> reportList
) {
    public static ReportDetailResponseDto fromEntity(UserReport userReport) {
        LocalDateTime createdAt = userReport.getCreatedAt();
        return new ReportDetailResponseDto(
                createdAt.getMonthValue(),
                createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                userReport.getReports().stream()
                        .map(ReportDetailDto::fromEntity)
                        .sorted(Comparator.comparingInt(ReportDetailDto::score).reversed())
                        .toList()
        );
    }
}
