package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.survey.entity.Solution;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReportDetailDto(
        @Schema(description = "점수", example = "5")
        int score,
        @Schema(description = "태그 이름", example = "SLEEP_PROBLEM")
        String tagName,
        @Schema(description = "제목", example = "무릎의 관절통증에 가장 큰 어려움을 겪어요")
        String title,
        SolutionDto solution
) {
    public static ReportDetailDto fromEntity(Report report) {
        String additional = report.getAdditional();
        Solution solution = report.getSolution();
        return new ReportDetailDto(
                report.getScore(),
                report.getTag().getName(),
                solution.getIsAdditional() ? additional + solution.getTitle() : solution.getTitle(),
                SolutionDto.fromEntity(report.getSolution())
        );
    }
}
