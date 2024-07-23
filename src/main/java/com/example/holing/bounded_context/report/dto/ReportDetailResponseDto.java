package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.UserReport;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record ReportDetailResponseDto(
        LocalDateTime createdAt,
        List<ReportDetailDto> reportList
) {
    public static ReportDetailResponseDto fromEntity(UserReport userReport) {
        return new ReportDetailResponseDto(
                userReport.getCreatedAt(),
                userReport.getReports().stream()
                        .map(ReportDetailDto::fromEntity)
                        .sorted(Comparator.comparingInt(ReportDetailDto::score).reversed())
                        .toList()
        );
    }
}
