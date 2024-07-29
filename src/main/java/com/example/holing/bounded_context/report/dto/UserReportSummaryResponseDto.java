package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.Locale;

@Schema(description = "사용자 리포트 요약 응답 DTO")
public record UserReportSummaryResponseDto(
        @Schema(description = "사용자 리포트 id", example = "1")
        Long id,
        @Schema(description = "월", example = "7")
        int month,
        @Schema(description = "주차", example = "1")
        int weekOfMonth,
        LocalDateTime createdAt,
        ReportDto top1Report
) {
    public static UserReportSummaryResponseDto fromEntity(UserReport userReport) {
        LocalDateTime createdAt = userReport.getCreatedAt();
        Report report = userReport.getReports().stream()
                .max(Comparator.comparingInt(Report::getScore))
                .get();

        return new UserReportSummaryResponseDto(
                userReport.getId(),
                createdAt.getMonthValue(),
                createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                createdAt,
                ReportDto.fromEntity(report)
        );
    }
}
