package com.example.holing.bounded_context.report.controller;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.report.api.ReportApi;
import com.example.holing.bounded_context.report.dto.ReportRequestDto;
import com.example.holing.bounded_context.report.dto.UserReportDetailResponseDto;
import com.example.holing.bounded_context.report.dto.UserReportScoreResponseDto;
import com.example.holing.bounded_context.report.dto.UserReportSummaryResponseDto;
import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.report.service.ReportService;
import com.example.holing.bounded_context.report.service.UserReportService;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.exception.UserExceptionCode;
import com.example.holing.bounded_context.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReportController implements ReportApi {

    private final UserService userService;
    private final ReportService reportService;
    private final UserReportService userReportService;
    private final JwtProvider jwtProvider;

    public ResponseEntity<List<UserReportScoreResponseDto>> score(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        List<UserReport> reports = userReportService.readScore(Long.parseLong(userId));
        List<UserReportScoreResponseDto> response = reports.stream().map(UserReportScoreResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<List<UserReportScoreResponseDto>> mateScore(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        if (user.getMate() == null) throw new GlobalException(UserExceptionCode.MATE_NOT_FOUND);

        List<UserReport> reports = userReportService.readScore(user.getMate().getId());
        List<UserReportScoreResponseDto> response = reports.stream().map(UserReportScoreResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<List<UserReportSummaryResponseDto>> summary(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        List<UserReportSummaryResponseDto> response = userReportService.readSummary(Long.parseLong(userId)).stream()
                .map(UserReportSummaryResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<List<UserReportSummaryResponseDto>> mateSummary(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        if (user.getMate() == null) throw new GlobalException(UserExceptionCode.MATE_NOT_FOUND);

        List<UserReportSummaryResponseDto> response = userReportService.readSummary(user.getMate().getId()).stream()
                .map(UserReportSummaryResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<UserReportDetailResponseDto> read(HttpServletRequest request, @PathVariable Long reportId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));

        UserReport userReport = userReportService.readDetail(user, reportId);
        UserReportDetailResponseDto response = UserReportDetailResponseDto.fromEntity(userReport);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<String> create(HttpServletRequest request, @RequestBody @Valid @Size(min = 6, max = 6) List<ReportRequestDto> reportRequestDtos) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        List<Report> reports = reportService.create(user, reportRequestDtos);
        return ResponseEntity.ok().body("리포트가 성공적으로 작성되었습니다.");
    }
}
