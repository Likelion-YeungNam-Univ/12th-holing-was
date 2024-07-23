package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;

public record ReportDetailDto(
        int score,
        String tagName,
        String content,
        SolutionDto solution
) {
    public static ReportDetailDto fromEntity(Report report) {
        return new ReportDetailDto(
                report.getScore(),
                report.getTag().getName(),
                report.getContent(),
                SolutionDto.fromEntity(report.getSolution())
        );
    }
}
