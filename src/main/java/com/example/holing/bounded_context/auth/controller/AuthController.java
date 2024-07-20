package com.example.holing.bounded_context.auth.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.auth.api.AuthApi;
import com.example.holing.bounded_context.auth.dto.OAuthTokenInfoDto;
import com.example.holing.bounded_context.auth.dto.OAuthUserInfoDto;
import com.example.holing.bounded_context.auth.dto.SignInRequestDto;
import com.example.holing.bounded_context.auth.service.OAuthService;
import com.example.holing.bounded_context.user.dto.UserInfoResponseDto;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final OAuthService oAuthService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    /**
     * 인증 코드 받기 요청: 소셜 로그인으로 인가 코드를 받을 수 있는 링크로 리다이렉션 합니다.<br>
     * 인가 코드 받기 요청의 응답은 HTTP 302 리다이렉트되어, redirect_uri에 GET 요청으로 전달됩니다.<br><br>
     * <p>
     * 인가 코드 받기: 로그인 동의 화면을 호출하고, 사용자 동의를 거쳐 인가 코드를 발급합니다.<br>
     * 사용자가 모든 필수 동의항목에 동의하고 [동의하고 계속하기] 버튼을 누른 경우 : redirect_uri로 인가 코드를 담은 쿼리 스트링 전달됩니다.<br>
     * 사용자가 동의 화면에서 [취소] 버튼을 눌러 로그인을 취소한 경우 : redirect_uri로 에러 정보를 담은 쿼리 스트링 전달됩니다.<br>
     * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code
     *
     * @return uri
     */
    @Override
    public RedirectView authorize() {
        String uri = oAuthService.getAuthorizeUri();
        return new RedirectView(uri);
    }

    /**
     * 토큰 발급 받기: 소셜 인증이 성공한 경우 이 API로 리다이렉트 되어 토큰을 발급합니다.
     *
     * @param code 인가 코드
     * @return TokenInfoDto 발급된 토큰
     * @throws HttpClientErrorException 토큰 발급 받기 api 요청 실패 시 발생합니다.
     */
    @Override
    public ResponseEntity<OAuthTokenInfoDto> token(@RequestParam("code") String code) {
        OAuthTokenInfoDto token = oAuthService.getToken(code);
        return ResponseEntity.ok().body(token);
    }

    /**
     * 로그인: 인증 서버 ccess token 으로 로그인을 진행합니다.<br>
     * 데이터베이스에 유저가 없는 경우 새 레코드를 생성합니다.<br>
     * 데이터베이스에 유저가 있는 경우 리소스 공급자의 데이터로 레코드를 업데이트합니다.<br>
     *
     * @param request access token 이 포함된 객체
     * @return UserInfoDto 유저 정보
     * @throws HttpClientErrorException 사용자 정보 받기 api 요청 실패 시 발생합니다.
     */
    @Override
    public ResponseEntity<UserInfoResponseDto> signIn(@RequestBody SignInRequestDto request) {
        OAuthUserInfoDto userInfo = oAuthService.getUserInfo(request.accessToken());
        User user = userService.saveOrUpdate(User.of(userInfo, request));

        String accessToken = jwtProvider.generatorAccessToken(user.getEmail(), user.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        UserInfoResponseDto response = UserInfoResponseDto.fromEntity(user);
        return ResponseEntity.ok().headers(headers).body(response);
    }

    /**
     * 회원 탈퇴: 회원 아이디로 회원탈퇴를 진행합니다<br>
     * 인증 서버의 아이디로 인증 서버와의 연결을 끊고 데이터베이스에 레코드를 지웁니다.
     *
     * @param userId 회원 아이디
     * @return
     * @throws HttpClientErrorException 연결 끊기 api 요청 실패 시 발생합니다.
     * @throws IllegalArgumentException 회원이 조회되지 않는 경우 발생합니다.
     */
    @Override
    public ResponseEntity<String> withdrawal(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        oAuthService.unlink(user.getSocialId());
        userService.delete(user);

        return ResponseEntity.ok().body("회원 탈퇴에 성공했습니다.");
    }
}
