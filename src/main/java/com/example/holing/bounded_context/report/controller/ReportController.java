package com.example.holing.bounded_context.report.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.report.dto.ReportDetailResponseDto;
import com.example.holing.bounded_context.report.dto.ReportRequestDto;
import com.example.holing.bounded_context.report.dto.ReportScoreResponseDto;
import com.example.holing.bounded_context.report.dto.ReportSummaryResponseDto;
import com.example.holing.bounded_context.report.entity.Report;
import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.report.service.ReportService;
import com.example.holing.bounded_context.report.service.UserReportService;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final UserService userService;
    private final ReportService reportService;
    private final UserReportService userReportService;
    private final JwtProvider jwtProvider;

    @GetMapping("/score")
    @Operation(summary = "리포트 점수 조회", description = "사용자가 리포트들의 점수를 조회하기 위한 API 입니다")
    public ResponseEntity<List<ReportScoreResponseDto>> score(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));

        List<UserReport> reports = userReportService.readAllWithReportByUser(user);
        List<ReportScoreResponseDto> response = reports.stream().map(ReportScoreResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "리포트 요약 조회", description = "사용자가 리포트들의 요약을 조회하기 위한 API 입니다")
    public ResponseEntity<List<ReportSummaryResponseDto>> summary(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));

        List<ReportSummaryResponseDto> response = userReportService.readAllByUser(user).stream()
                .map(ReportSummaryResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{reportId}")
    @Operation(summary = "리포트 상세 조회", description = "사용자가 리포트를 상세 조회하기 위한 API 입니다")
    public ResponseEntity<ReportDetailResponseDto> read(HttpServletRequest request, @PathVariable Long reportId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));

        UserReport userReport = userReportService.readWithReportAndSolutionById(reportId);
        ReportDetailResponseDto response = ReportDetailResponseDto.fromEntity(userReport);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("")
    @Operation(summary = "리포트 생성", description = "사용자가 자가 테스트를 하여 리포트를 생성하기 위한 API 입니다.")
    public ResponseEntity<String> create(HttpServletRequest request, @RequestBody @Valid @Size(min = 6, max = 6) List<ReportRequestDto> reportRequestDtos) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        List<Report> reports = reportService.create(user, reportRequestDtos);
        return ResponseEntity.ok().body("리포트가 성공적으로 작성되었습니다.");
    }
}
