package com.example.holing.bounded_context.report.api;

import com.example.holing.bounded_context.report.dto.ReportRequestDto;
import com.example.holing.bounded_context.report.dto.UserReportDetailResponseDto;
import com.example.holing.bounded_context.report.dto.UserReportScoreResponseDto;
import com.example.holing.bounded_context.report.dto.UserReportSummaryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "[리포트 관련 API]", description = "리포트 조회 및 생성 API")
public interface ReportApi {
    @GetMapping("/user/me/reports/score")
    @Operation(summary = "리포트 점수 조회", description = """
            사용자가 본인의 리포트 점수들을 조회하기 위한 API 입니다
            리포트의 태그와 점수 목록을 반환합니다.
            """)
    ResponseEntity<List<UserReportScoreResponseDto>> score(HttpServletRequest request);

    @GetMapping("/user/mate/reports/score")
    @Operation(summary = "짝꿍 리포트 점수 조회", description = """
            사용자가 짝꿍의 리포트 점수들을 조회하기 위한 API 입니다
            리포트의 태그와 점수 목록을 반환합니다.
            """)
    public ResponseEntity<List<UserReportScoreResponseDto>> mateScore(HttpServletRequest request);

    @GetMapping("/user/me/reports/summary")
    @Operation(summary = "본인 리포트 요약 조회", description = """
            사용자가 본인의 리포트 요약들을 조회하기 위한 API 입니다
            테스트 결과 점수가 가장 높은 증상의 솔루션 제목을 반환합니다.
            """)
    public ResponseEntity<List<UserReportSummaryResponseDto>> summary(HttpServletRequest request);

    @GetMapping("/user/mate/reports/summary")
    @Operation(summary = "짝꿍 리포트 요약 조회", description = """
            사용자가 짝꿍의 리포트 요약들을 조회하기 위한 API 입니다
            테스트 결과 점수가 가장 높은 증상의 솔루션 제목을 반환합니다.
            """)
    public ResponseEntity<List<UserReportSummaryResponseDto>> mateSummary(HttpServletRequest request);

    @GetMapping("/reports/{reportId}")
    @Operation(summary = "리포트 상세 조회", description = "사용자가 리포트를 상세 조회하기 위한 API 입니다")
    public ResponseEntity<UserReportDetailResponseDto> read(HttpServletRequest request, @PathVariable Long reportId);

    @PostMapping("/reports")
    @Operation(summary = "리포트 생성", description = """
            사용자의 증상 테스트 결과로 리포트를 생성하기 위한 API 입니다.
            점수와 사용자 입력 항목을 받습니다. 사용자 입력 항목이 없는 경우 빈 문자열을 입력합니다. 
            """)
    public ResponseEntity<String> create(HttpServletRequest request, @RequestBody @Valid @Size(min = 6, max = 6) List<ReportRequestDto> reportRequestDtos);
}
