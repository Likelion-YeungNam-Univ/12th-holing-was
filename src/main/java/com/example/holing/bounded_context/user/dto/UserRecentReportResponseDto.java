package com.example.holing.bounded_context.user.dto;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.survey.entity.Solution;
import com.example.holing.bounded_context.user.entity.Gender;
import com.example.holing.bounded_context.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Schema(description = "사용자 및 최신 리포트 정보 응답 DTO")
public record UserRecentReportResponseDto(
        @Schema(description = "유저 닉네임", example = "myNickname")
        String nickname,
        @Schema(description = "유저 성별", example = "MALE")
        Gender gender,
        @Schema(description = "프로필 이미지 URL", example = "http://example.com/image.jpg")
        String profileImgUrl,
        @Schema(description = "짝꿍 닉네임", example = "mateNickname")
        String mateNickname,
        @Schema(description = "유저 가입기간", example = "100")
        long dDay,
        UserReportDto userRecentReport
) {
    public static UserRecentReportResponseDto of(User user, UserReport userReport) {
        return new UserRecentReportResponseDto(
                user.getNickname(),
                user.getGender(),
                user.getProfileImgUrl(),
                user.getMate().getNickname(),
                ChronoUnit.DAYS.between(user.getCreatedAt().toLocalDate(), LocalDate.now()),
                UserReportDto.fromEntity(userReport)
        );
    }

    public record UserReportDto(
            @Schema(description = "사용자 리포트 id", example = "1")
            Long id,
            @Schema(description = "월", example = "7")
            int month,
            @Schema(description = "주차", example = "1")
            int weekOfMonth,
            @Schema(description = "모든 리포트 총 점수", example = "50")
            int totalScore,
            ReportDto top1Report,
            ReportDto top2Report

    ) {
        public static UserReportDto fromEntity(UserReport userReport) {
            LocalDateTime createdAt = userReport.getCreatedAt();
            List<Report> reports = userReport.getReports().stream()
                    .sorted(Comparator.comparingInt(Report::getScore).reversed()).toList();


            return new UserReportDto(
                    userReport.getId(),
                    createdAt.getMonthValue(),
                    createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                    reports.stream().mapToInt(Report::getScore).sum(),
                    ReportDto.fromEntity(reports.get(0)),
                    ReportDto.fromEntity(reports.get(1))
            );
        }

        public record ReportDto(
                @Schema(description = "점수", example = "5")
                int score,
                @Schema(description = "증상 대표 이미지", example = "url")
                String imgUrl,
                @Schema(description = "제목", example = "무릎의 관절통증에 가장 큰 어려움을 겪어요")
                String title
        ) {
            public static ReportDto fromEntity(Report report) {
                String additional = report.getAdditional();
                Solution solution = report.getSolution();
                return new ReportDto(
                        report.getScore(),
                        report.getTag().getImgUrl(),
                        solution.getIsAdditional() ? additional + solution.getTitle() : solution.getTitle()
                );
            }
        }
    }
}
