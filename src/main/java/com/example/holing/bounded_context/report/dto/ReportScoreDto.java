package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReportScoreDto(
        @Schema(description = "점수", example = "5")
        int score,
        @Schema(description = "태그 이름", example = "SLEEP_PROBLEM")
        String tagName
) {
    public static ReportScoreDto fromEntity(Report report) {
        return new ReportScoreDto(
                report.getScore(),
                report.getTag().getName()
        );
    }
}
