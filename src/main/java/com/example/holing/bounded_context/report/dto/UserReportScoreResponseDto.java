package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Schema(description = "사용자 리포트 점수 응답 DTO")
public record UserReportScoreResponseDto(
        @Schema(description = "월", example = "7")
        int month,
        @Schema(description = "주차", example = "1")
        int weekOfMonth,
        List<ReportScoreDto> reportList
) {
    public static UserReportScoreResponseDto fromEntity(UserReport userReport) {
        LocalDateTime createdAt = userReport.getCreatedAt();
        List<ReportScoreDto> list = userReport.getReports().stream()
                .map(ReportScoreDto::fromEntity)
                .collect(Collectors.toList());
        if (list.size() == 5) list.add(new ReportScoreDto(0, "PERIOD"));

        return new UserReportScoreResponseDto(
                createdAt.getMonthValue(),
                createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                list
        );
    }

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
}
