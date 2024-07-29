package com.example.holing.bounded_context.auth.api;

import com.example.holing.bounded_context.auth.dto.OAuthTokenInfoDto;
import com.example.holing.bounded_context.auth.dto.SignInRequestDto;
import com.example.holing.bounded_context.user.dto.UserInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/auth")
@Tag(name = "[인증 관련 API]", description = "인증, 로그인 및 회원가입, 탈퇴 API")
public interface AuthApi {
    @GetMapping("/authorize")
    @Operation(summary = "소셜 인증", description = "사용자가 소셜 인증을 받기 위한 API 입니다.<br>로그인 후 서비스 필수 항목에 동의가 완료되면 /auth/token 을 호출합니다.")
    RedirectView authorize();

    @GetMapping("/token")
    @Operation(summary = "소셜 인증 토큰 반환", description = "사용자가 소셜 인증 토큰을 받기 위한 API 입니다.")
    ResponseEntity<OAuthTokenInfoDto> token(@RequestParam("code") String code);

    @PostMapping("/sign-in")
    @Operation(summary = "로그인 및 회원가입", description = "사용자가 로그인 및 회원가입을 위한 API 입니다.<br>회원가입의 경우 자가테스트의 사용자 정보가 필요합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 로그인 성공"),
            @ApiResponse(responseCode = "400", description = "소셜 인증 서버에서 오류가 발생한 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "API_ERROR",
                                                                                    "cause": "401 Unauthorized: \\"{\\"msg\\":\\"this access token does not exist\\",\\"code\\":-401}\\""
                                                                                }
                                    """)
                    }))
    })
    ResponseEntity<UserInfoResponseDto> signIn(@RequestBody SignInRequestDto request, @RequestParam("code") String code);

    @DeleteMapping("/withdrawal")
    @Operation(summary = "회원 탈퇴[임시]", description = "사용자가 서비스와 연결을 끊고 회원을 탈퇴하기 위한 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "소셜 인증 서버에서 오류가 발생한 경우",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                                    "name": "API_ERROR",
                                                                                    "cause": "401 Unauthorized: \\"{\\"msg\\":\\"this access token does not exist\\",\\"code\\":-401}\\""
                                                                                }
                                    """)
                    }))
    })
    ResponseEntity<String> withdrawal(HttpServletRequest request);
}
