package com.example.holing.bounded_context.report.api;

import com.example.holing.bounded_context.report.dto.ReportRequestDto;
import com.example.holing.bounded_context.report.dto.UserReportDetailResponseDto;
import com.example.holing.bounded_context.report.dto.UserReportScoreResponseDto;
import com.example.holing.bounded_context.report.dto.UserReportSummaryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "[리포트 관련 API]", description = "리포트 조회 및 생성 API")
public interface ReportApi {
    @GetMapping("/user/me/reports/score")
    @Operation(summary = "본인의 증상 테스트 기록 점수 조회", description = """
            사용자가 본인의 모든 증상 테스트 기록의 점수를 조회하기 위한 API 입니다
            각 테스트 기록의 모든 점수들이 반환됩니다.
            기록이 없는 경우 빈 객체가 반환됩니다.
            """)
    @ApiResponse(responseCode = "200", description = "본인 증상 테스트 기록 점수 조회 성공")
    ResponseEntity<List<UserReportScoreResponseDto>> score(HttpServletRequest request);

    @GetMapping("/user/mate/reports/score")
    @Operation(summary = "짝꿍의 증상 테스트 기록 점수 조회", description = """
            사용자가 짝꿍의 모든 증상 테스트 기록의 점수를 조회하기 위한 API 입니다
            각 테스트 기록의 모든 점수들이 반환됩니다.
            기록이 없는 경우 빈 객체가 반환됩니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "짝꿍 증상 테스트 기록 점수 조회 성공"),
            @ApiResponse(responseCode = "404", description = "짝꿍이 존재하지 않는 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "MATE_NOT_FOUND",
                                                                                    "cause": "사용자의 짝꿍을 찾을 수 없습니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<List<UserReportScoreResponseDto>> mateScore(HttpServletRequest request);

    @GetMapping("/user/me/reports/summary")
    @Operation(summary = "본인의 증상 테스트 기록 요약 조회", description = """
            사용자가 본인의 모든 증상 테스트 기록의 요약을 조회하기 위한 API 입니다
            각 테스트 기록에서 가장 점수가 높은 솔루션의 제목이 반환됩니다.
            기록이 없는 경우 빈 객체가 반환됩니다.
            """)
    @ApiResponse(responseCode = "200", description = "본인 증상 테스트 기록 요약 조회 성공")
    ResponseEntity<List<UserReportSummaryResponseDto>> summary(HttpServletRequest request);

    @GetMapping("/user/mate/reports/summary")
    @Operation(summary = "짝꿍의 증상 테스트 기록 요약 조회", description = """
            사용자가 짝꿍의 모든 증상 테스트 기록의 요약을 조회하기 위한 API 입니다
            각 테스트 기록에서 가장 점수가 높은 솔루션의 제목이 반환됩니다.
            기록이 없는 경우 빈 객체가 반환됩니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "짝꿍 증상 테스트 기록 요약 조회 성공"),
            @ApiResponse(responseCode = "404", description = "짝꿍이 존재하지 않는 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "MATE_NOT_FOUND",
                                                                                    "cause": "사용자의 짝꿍을 찾을 수 없습니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<List<UserReportSummaryResponseDto>> mateSummary(HttpServletRequest request);

    @GetMapping("/reports/{reportId}")
    @Operation(summary = "증상 테스트 기록 상세 조회", description = """
            사용자가 증상 테스트 기록을 상세 조회하기 위한 API 입니다
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "증상 테스트 기록 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "증상 테스트 기록이 존재하지 않는 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "TEST_HISTORY_NOT_FOUND_BY_ID",
                                                                                    "cause": "해당 증상 테스트 기록이 존재하지 않습니다."
                                                                                }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "증상 테스트 기록에 접근할 수 없는 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "ACCESS_DENIED_TO_TEST_HISTORY",
                                                                                    "cause": "해당 증상 테스트 기록에 접근 권한이 없습니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<UserReportDetailResponseDto> read(HttpServletRequest request, @PathVariable Long reportId);

    @PostMapping("/reports")
    @Operation(summary = "리포트 생성", description = """
            사용자의 증상 테스트 점수로 증상 테스트 기록과 결과를 생성하기 위한 API 입니다.
            점수와 사용자 입력 항목을 받습니다. 사용자 입력 항목이 없는 경우 빈 문자열을 입력합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리포트 생성 성공"),
            @ApiResponse(responseCode = "400", description = "리포트 생성 조건을 만족하지 못한 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "USER_REPORT_PERIOD_NOT_MET",
                                                                                    "cause": "마지막 증상 테스트 이후 7일이 경과해야 합니다."
                                                                                }
                                    """),
                    })),
    })
    ResponseEntity<String> create(HttpServletRequest request, @RequestBody List<ReportRequestDto> reportRequestDtos);
}
