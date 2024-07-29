package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.survey.entity.Solution;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Schema(description = "사용자 리포트 상세 응답 DTO")
public record UserReportDetailResponseDto(
        @Schema(description = "월", example = "7")
        int month,
        @Schema(description = "주차", example = "1")
        int weekOfMonth,
        List<ReportDto> reportList
) {
    public static UserReportDetailResponseDto fromEntity(UserReport userReport) {
        LocalDateTime createdAt = userReport.getCreatedAt();
        return new UserReportDetailResponseDto(
                createdAt.getMonthValue(),
                createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                userReport.getReports().stream()
                        .map(ReportDto::fromEntity)
                        .sorted(Comparator.comparingInt(ReportDto::score).reversed())
                        .toList()
        );
    }

    public record ReportDto(
            @Schema(description = "점수", example = "5")
            int score,
            @Schema(description = "태그 이름", example = "SLEEP_PROBLEM")
            String tagName,
            @Schema(description = "제목", example = "무릎의 관절통증에 가장 큰 어려움을 겪어요")
            String title,
            SolutionDto solution
    ) {
        public static ReportDto fromEntity(Report report) {
            String additional = report.getAdditional();
            Solution solution = report.getSolution();
            return new ReportDto(
                    report.getScore(),
                    report.getTag().getName(),
                    solution.getIsAdditional() ? additional + solution.getTitle() : solution.getTitle(),
                    SolutionDto.fromEntity(report.getSolution())
            );
        }
    }
}
