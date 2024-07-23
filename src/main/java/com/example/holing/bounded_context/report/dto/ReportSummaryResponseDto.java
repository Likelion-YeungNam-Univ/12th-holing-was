package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;

import java.time.LocalDateTime;
import java.util.Comparator;

public record ReportSummaryResponseDto(
        Long id,
        LocalDateTime createdAt,
        String title
) {
    public static ReportSummaryResponseDto fromEntity(UserReport userReport) {
        return new ReportSummaryResponseDto(
                userReport.getId(),
                userReport.getCreatedAt(),
                userReport.getReports().stream()
                        .max(Comparator.comparingInt(Report::getScore))
                        .get().getSolution().getTitle()
        );
    }
}
