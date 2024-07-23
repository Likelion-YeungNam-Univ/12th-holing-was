package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.UserReport;

import java.time.LocalDateTime;
import java.util.List;

public record ReportScoreResponseDto(
        LocalDateTime createdAt,
        List<ReportScoreDto> reportList
) {
    public static ReportScoreResponseDto fromEntity(UserReport userReport) {
        return new ReportScoreResponseDto(
                userReport.getCreatedAt(),
                userReport.getReports().stream()
                        .map(ReportScoreDto::fromEntity).toList()
        );
    }
}
