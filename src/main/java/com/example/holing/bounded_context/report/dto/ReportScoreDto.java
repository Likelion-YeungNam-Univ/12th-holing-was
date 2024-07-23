package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;

public record ReportScoreDto(
        int score,
        String tagName
) {
    public static ReportScoreDto fromEntity(Report report) {
        return new ReportScoreDto(
                report.getScore(),
                report.getTag().getName()
        );
    }
}
