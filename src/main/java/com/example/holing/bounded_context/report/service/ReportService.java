package com.example.holing.bounded_context.report.service;

import com.example.holing.bounded_context.report.dto.ReportRequestDto;
import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.report.repository.ReportRepository;
import com.example.holing.bounded_context.survey.entity.Tag;
import com.example.holing.bounded_context.survey.repository.TagRepository;
import com.example.holing.bounded_context.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserReportService userReportService;
    private final ReportRepository reportRepository;
    private final TagRepository tagRepository;

    @Transactional
    public List<Report> create(User user, List<ReportRequestDto> dto) {
//        if (!validateReportByPeriod(user, dto)) throw new IllegalArgumentException("알맞은 검사가 아닙니다.");
        UserReport userReport = userReportService.create(user);

        List<Tag> tagList = tagRepository.findAllWithSolution();
        Map<Long, Tag> tagMap = tagList.stream()
                .collect(Collectors.toMap(Tag::getId, tag -> tag));

        return dto.stream().map(reportRequestDto -> {
            Tag tag = tagMap.get(reportRequestDto.tagId());

            Report report = Report.builder()
                    .userReport(userReport)
                    .score(reportRequestDto.score())
                    .tag(tag)
                    .solution(tag.getSolutions().stream()
                            .filter(solution -> solution.getMinScore() <= reportRequestDto.score())
                            .min((s1, s2) -> s2.getMinScore() - s1.getMinScore())
                            .get())
                    .additional(reportRequestDto.additional() == null ? null : reportRequestDto.additional())
                    .build();

            return reportRepository.save(report);
        }).toList();
    }

//    public Boolean validateReportByPeriod(User user, List<ReportRequestDto> reportRequestDtos) {
//        ReportRequestDto reportRequestDto = reportRequestDtos.get(5);
//        if (user.getIsPeriod()) {
//            return reportRequestDto.score() != 0;
//        }
//        return reportRequestDto.score() == 0;
//    }
}
