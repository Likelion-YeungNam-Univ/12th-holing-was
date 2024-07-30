package com.example.holing.bounded_context.report.service;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.report.exception.ReportExceptionCode;
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

    public Optional<UserReport> readRecentByUser(User user) {
        return userReportRepository.findFirstByUserOrderByCreatedAtDesc(user);
    }

    public List<UserReport> readScore(Long userId) {
        return userReportRepository.findAllWithReportByUser(userId);
    }

    public List<UserReport> readSummary(Long userId) {
        return userReportRepository.findAllWithReportAndSolutionByUser(userId);
    }

    public UserReport readDetail(User user, Long id) {
        UserReport userReport = userReportRepository.findWithReportAndSolutionById(id)
                .orElseThrow(() -> new GlobalException(ReportExceptionCode.TEST_HISTORY_NOT_FOUND_BY_ID));

        if (user != userReport.getUser()) throw new GlobalException(ReportExceptionCode.ACCESS_DENIED_TO_TEST_HISTORY);

        return userReport;
    }

    public List<Tag> getUserRecentReportTag(User user) {
        List<UserReport> userReports = readScore(user.getId());
        List<Report> reports = userReports.get(userReports.size() - 1).getReports();

        List<Tag> tags = reports.stream()
                .sorted(Comparator.comparingInt(Report::getScore).reversed())
                .map(Report::getTag).collect(Collectors.toList());

        tags.add(tagRepository.findById(7L).get());

        return tags;
    }

    @Transactional
    public UserReport create(User user) {
        if (!isWeekInterval(user)) throw new GlobalException(ReportExceptionCode.USER_REPORT_PERIOD_NOT_MET);

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
