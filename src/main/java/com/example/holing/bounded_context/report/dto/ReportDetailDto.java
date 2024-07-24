package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.survey.entity.Solution;

public record ReportDetailDto(
        int score,
        String tagName,
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
