package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.survey.entity.Solution;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.Locale;

public record ReportSummaryResponseDto(
        @Schema(description = "리포트 id", example = "1")
        Long id,
        @Schema(description = "월", example = "7")
        int month,
        @Schema(description = "주차", example = "1")
        int weekOfMonth,
        @Schema(description = "제목", example = "무릎의 관절통증에 가장 큰 어려움을 겪어요")
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
