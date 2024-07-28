package com.example.holing.bounded_context.report.service;

import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.report.repository.UserReportRepository;
import com.example.holing.bounded_context.survey.entity.Tag;
import com.example.holing.bounded_context.survey.repository.TagRepository;
import com.example.holing.bounded_context.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReportService {
    private final UserReportRepository userReportRepository;
    private final TagRepository tagRepository;

    public UserReport readWithReportAndSolutionById(Long id) {
        return userReportRepository.findWithReportAndSolutionById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자 리포트를 찾을 수 없습니다."));
    }

    public UserReport readRecentByUser(User user) {
        return userReportRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new IllegalArgumentException("최신 사용자 리포트를 찾을 수 없습니다."));
    }

    public List<UserReport> readAllWithReportByUser(User user) {
        return userReportRepository.findAllWithReportByUser(user);
    }

    public List<UserReport> readAllByUser(User user) {
        return userReportRepository.findAllWithReportAndSolutionByUser(user);
    }

    public List<Tag> getUserRecentReportTag(User user) {
        List<UserReport> userReports = readAllWithReportByUser(user);
        List<Report> reports = userReports.get(userReports.size() - 1).getReports();

        List<Tag> tags = reports.stream()
                .sorted(Comparator.comparingInt(Report::getScore).reversed())
                .map(Report::getTag).collect(Collectors.toList());

        tags.add(tagRepository.findById(7L).get());

        return tags;
    }

    @Transactional
    public UserReport create(User user) {
        if (!isWeekInterval(user)) throw new IllegalArgumentException("일주일이 지나지 않았습니다.");

        UserReport userReport = UserReport.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return userReportRepository.save(userReport);
    }

    public Boolean isWeekInterval(User user) {
        Optional<UserReport> userReport = userReportRepository.findFirstByUserOrderByCreatedAtDesc(user);
        return userReport.map(report -> report.getCreatedAt().isBefore(LocalDateTime.now().minusWeeks(1)))
                .orElse(true);
    }
}
