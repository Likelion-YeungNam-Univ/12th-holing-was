package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.survey.entity.Solution;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.Locale;

public record ReportSummaryResponseDto(
        Long id,
        int month,
        int weekOfMonth,
        String title
) {
    public static ReportSummaryResponseDto fromEntity(UserReport userReport) {
        LocalDateTime createdAt = userReport.getCreatedAt();
        Report report = userReport.getReports().stream()
                .max(Comparator.comparingInt(Report::getScore))
                .get();
        String additional = report.getAdditional();
        Solution solution = report.getSolution();

        return new ReportSummaryResponseDto(
                userReport.getId(),
                createdAt.getMonthValue(),
                createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                solution.getIsAdditional() ? additional + solution.getTitle() : solution.getTitle()
        );
    }
}
