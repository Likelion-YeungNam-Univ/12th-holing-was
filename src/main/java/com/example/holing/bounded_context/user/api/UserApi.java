package com.example.holing.bounded_context.user.api;

import com.example.holing.bounded_context.user.dto.UserExchangeRequestDto;
import com.example.holing.bounded_context.user.dto.UserInfoResponseDto;
import com.example.holing.bounded_context.user.dto.UserRecentReportResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Tag(name = "[사용자 관련 API]", description = "사용자 조회 및 짝꿍 연결 API")
public interface UserApi {

    @GetMapping("/me/reports")
    @Operation(summary = "본인 정보 및 최신 리포트 조회", description = "사용자가 본인의 정보와 리포트를 조회하기 위한 API 입니다")
    @ApiResponse(responseCode = "200", description = "본인 정보 및 최신 리포트 조회 성공")
    ResponseEntity<UserRecentReportResponseDto> readWithReport(HttpServletRequest request);

    @GetMapping("/mate/reports")
    @Operation(summary = "짝꿍 정보 및 최신 리포트 조회", description = "사용자가 짝꿍의 정보와 리포트를 조회하기 위한 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "짝꿍 정보 및 최신 리포트 조회 성공"),
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
    ResponseEntity<UserRecentReportResponseDto> readMateWithReport(HttpServletRequest request);

    @GetMapping("/me")
    @Operation(summary = "본인 정보 조회", description = "사용자가 본인의 정보를 조회하기 위한 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "본인 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 조회할 수 없는 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "USER_NOT_FOUND",
                                                                                    "cause": "사용자를 찾을 수 없습니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<UserInfoResponseDto> read(HttpServletRequest request);

//    /**
//     * 사용자 정보 조회 : 사용자가 다른 사용자의 정보를 조회하기 위한 메서드입니다.
//     *
//     * @param userId 대상 사용자 아이디
//     * @return UserInfoDto 사용자 정보
//     */
//    @GetMapping("/{userId}")
//    ResponseEntity<UserInfoDto> read(@PathVariable Long userId);

    @PatchMapping("/connect/{socialId}")
    @Operation(summary = "짝꿍 연결", description = "사용자가 대상 사용자와 짝꿍으로 연결하기 위한 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "짝꿍 연결 성공"),
            @ApiResponse(responseCode = "400", description = "정상적인 코드가 아닌 경우 코드는 10자리 숫자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "SYSTEM_ERROR",
                                                                                    "cause": "400 BAD_REQUEST Validation failure"
                                                                                }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "사용자 또는 대상 사용자를 조회할 수 없는 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "USER_NOT_FOUND", value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "USER_NOT_FOUND",
                                                                                    "cause": "사용자를 찾을 수 없습니다."
                                                                                }
                                    """),
                            @ExampleObject(name = "TARGET_NOT_FOUND", value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "TARGET_NOT_FOUND",
                                                                                    "cause": "대상자를 찾을 수 없습니다."
                                                                                }
                                    """),
                    })),
            @ApiResponse(responseCode = "409", description = "사용자의 짝꿍이 존재하거나, 대상자의 짝꿍이 존재하는 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "USER_MATE_EXISTS", value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "USER_MATE_EXISTS",
                                                                                    "cause": "사용자의 짝꿍이 이미 존재합니다."
                                                                                }
                                    """),
                            @ExampleObject(name = "TARGET_MATE_EXISTS", value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "TARGET_MATE_EXISTS",
                                                                                    "cause": "대상자의 짝꿍이 이미 존재합니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<String> connectMate(HttpServletRequest request, @Valid @NotNull @Pattern(regexp = "^\\d{10}$") @PathVariable String socialId);

    @PatchMapping("/disconnect")
    @Operation(summary = "짝꿍 해제", description = "사용자가 기존 짝궁과 연결을 해제하기 위한 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "짝꿍 해제 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 짝꿍을 조회할 수 없을 때 발생합니다.",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "USER_NOT_FOUND", value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "USER_NOT_FOUND",
                                                                                    "cause": "사용자를 찾을 수 없습니다."
                                                                                }
                                    """),
                            @ExampleObject(name = "MATE_NOT_FOUND", value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "MATE_NOT_FOUND",
                                                                                    "cause": "사용자의 짝꿍을 찾을 수 없습니다."
                                                                                }
                                    """),
                    })),
    })
    ResponseEntity<String> disconnectMate(HttpServletRequest request);

    @PostMapping("/product/exchanges")
    @Operation(summary = "상품 교환", description = "사용자가 마이페이지에서 상품을 교환하기 위한 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 교환 성공"),
            @ApiResponse(responseCode = "400", description = "사용자의 포인트가 상품의 포인트보다 부족한 경우 발생합니다.",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "LACK_OF_POINT", value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-31T22:04:33.1247853",
                                                                                    "name": "LACK_OF_POINT",
                                                                                    "cause": "포인트가 부족합니다."
                                                                                }
                                    """)
                    })),
    })
    ResponseEntity<UserInfoResponseDto> exchange(HttpServletRequest request, @RequestBody UserExchangeRequestDto userExchangeRequestDto);

    @PatchMapping("/self-test")
    @Operation(summary = "유저 자가 테스트 완료 처리", description = "로그인 한 사용자의 자가 진단 테스트 유무를 완료 처리하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "자가 진단 테스트 완료")
    ResponseEntity<UserInfoResponseDto> completeSelfTest(HttpServletRequest request);
}
