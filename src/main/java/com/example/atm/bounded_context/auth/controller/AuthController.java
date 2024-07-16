package com.example.atm.bounded_context.auth.controller;

import com.example.atm.bounded_context.auth.dto.OAuthTokenInfoDto;
import com.example.atm.bounded_context.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final OAuthService oAuthService;

    /**
     * 인증 코드 받기 요청: 소셜 로그인으로 인가 코드를 받을 수 있는 링크로 리다이렉션 합니다.<br>
     * 인가 코드 받기 요청의 응답은 HTTP 302 리다이렉트되어, redirect_uri에 GET 요청으로 전달됩니다.<br>
     * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code
     *
     * @return uri
     */
    @GetMapping("/authorize")
    public RedirectView authorize() {
        String uri = oAuthService.getAuthorizeUri();
        return new RedirectView(uri);
    }

    /**
     * 인가 코드 받기: 로그인 동의 화면을 호출하고, 사용자 동의를 거쳐 인가 코드를 발급합니다.<br>
     * 사용자가 모든 필수 동의항목에 동의하고 [동의하고 계속하기] 버튼을 누른 경우 : redirect_uri로 인가 코드를 담은 쿼리 스트링 전달됩니다.<br>
     * 사용자가 동의 화면에서 [취소] 버튼을 눌러 로그인을 취소한 경우 : redirect_uri로 에러 정보를 담은 쿼리 스트링 전달됩니다.<br>
     *
     * @param code 인가 코드
     * @return TokenInfoDto 발급된 토큰
     * @throws HttpClientErrorException api 요청 실패 시 발생합니다.
     */
    @GetMapping("/token")
    public ResponseEntity<OAuthTokenInfoDto> token(@RequestParam("code") String code) {
        OAuthTokenInfoDto token = oAuthService.getToken(code);
        return ResponseEntity.ok().body(token);
    }
}
