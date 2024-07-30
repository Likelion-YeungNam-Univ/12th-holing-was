package com.example.holing.bounded_context.user.dto;

import com.example.holing.bounded_context.report.dto.ReportDto;
import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
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
import java.util.Optional;

@Schema(description = "사용자 및 최신 리포트 정보 응답 DTO")
public record UserRecentReportResponseDto(
        @Schema(description = "유저 닉네임", example = "myNickname")
        String nickname,
        @Schema(description = "유저 성별", example = "MALE")
        Gender gender,
        @Schema(description = "프로필 이미지 URL", example = "http://example.com/profileImage.jpg")
        String profileImgUrl,
        @Schema(description = "짝꿍 닉네임", example = "mateNickname")
        String mateNickname,
        @Schema(description = "유저 가입기간", example = "100")
        long dDay,
        UserReportDto userRecentReport
) {
    public static UserRecentReportResponseDto of(User user, Optional<UserReport> userReport) {
        return new UserRecentReportResponseDto(
                user.getNickname(),
                user.getGender(),
                user.getProfileImgUrl(),
                user.getMate() != null ? user.getMate().getNickname() : null,
                ChronoUnit.DAYS.between(user.getCreatedAt().toLocalDate(), LocalDate.now()),
                userReport.map(UserReportDto::fromEntity).orElse(null)
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
            @Schema(description = "백그라운드 이미지 URL", example = "http://example.com/backgroundImage.jpg")
            String backgroundImgUrl,
            ReportDto top1Report,
            ReportDto top2Report

    ) {
        public static UserReportDto fromEntity(UserReport userReport) {
            LocalDateTime createdAt = userReport.getCreatedAt();
            List<Report> reports = userReport.getReports().stream()
                    .sorted(Comparator.comparingInt(Report::getScore).reversed()).toList();

            String imgUrl = userReport.getUser().getGender() == Gender.MALE ?
                    reports.get(0).getTag().getManImgUrl() :
                    reports.get(0).getTag().getWomanImgUrl();

            return new UserReportDto(
                    userReport.getId(),
                    createdAt.getMonthValue(),
                    createdAt.get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
                    reports.stream().mapToInt(Report::getScore).sum(),
                    imgUrl,
                    ReportDto.fromEntity(reports.get(0)),
                    ReportDto.fromEntity(reports.get(1))
            );
        }
    }
}
