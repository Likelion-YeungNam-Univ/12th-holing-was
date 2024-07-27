package com.example.holing.bounded_context.user.dto;

import com.example.holing.bounded_context.report.dto.ReportDto;
import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public record UserResponseDto(
        String nickname,
        String mateNickname,
        long dDay,
        int totalScore,
        ReportDto reportTop1,
        ReportDto reportTop2,
        Long id,
        LocalDateTime createdAt
) {
    public static UserResponseDto of(User user, UserReport userReport) {
        List<Report> reports = userReport.getReports().stream()
                .sorted(Comparator.comparingInt(Report::getScore).reversed()).toList();

        return new UserResponseDto(
                user.getNickname(),
                user.getMate().getNickname(),
                ChronoUnit.DAYS.between(user.getCreatedAt().toLocalDate(), LocalDate.now()),
                reports.stream().mapToInt(Report::getScore).sum(),
                ReportDto.fromEntity(reports.get(0)),
                ReportDto.fromEntity(reports.get(1)),
                userReport.getId(),
                userReport.getCreatedAt()
        );
    }
}
