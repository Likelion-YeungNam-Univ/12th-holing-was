package com.example.atm.bounded_context.user.controller;

import com.example.atm.base.jwt.JwtProvider;
import com.example.atm.bounded_context.user.dto.UserInfoDto;
import com.example.atm.bounded_context.user.entity.User;
import com.example.atm.bounded_context.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("테스트 성공");
    }

    /**
     * 내 정보 조회 : 애플리케이션 access token(JWT) 로 사용자 정보를 조회합니다.
     *
     * @param request
     * @return UserInfoDto
     */
    @GetMapping("/me")
    ResponseEntity<UserInfoDto> read(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        UserInfoDto response = UserInfoDto.fromEntity(user);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 사용자 정보 조회 : userId 로 사용자 정보를 조회합니다.
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    ResponseEntity<UserInfoDto> read(@PathVariable Long userId) {

        User user = userService.read(userId);
        UserInfoDto response = UserInfoDto.fromEntity(user);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 짝궁 연결 : 로그인한 사용자와 userId의 관계를 짝꿍으로 연결합니다.
     *
     * @param request
     * @param userId
     * @return
     */
    @PostMapping("/connect/{userId}")
    public ResponseEntity<String> connectMate(HttpServletRequest request, @PathVariable Long userId) {
        String accessToken = jwtProvider.getToken(request);
        String acceptUserId = jwtProvider.getUserId(accessToken);

        userService.connectMate(Long.parseLong(acceptUserId), userId);

        return ResponseEntity.ok().body("짝꿍 연결에 성공했습니다.");
    }

    /**
     * 짝궁 해제 : 로그인한 사용자와 userId의 짝꿍 관계를 해제합니다.
     *
     * @param request
     * @param userId
     * @return
     */
    @PostMapping("/disconnect/{userId}")
    public ResponseEntity<String> disconnectMate(HttpServletRequest request, @PathVariable Long userId) {
        String accessToken = jwtProvider.getToken(request);
        String acceptUserId = jwtProvider.getUserId(accessToken);

        userService.disconnectMate(Long.parseLong(acceptUserId), userId);

        return ResponseEntity.ok().body("짝꿍 해제에 성공했습니다.");
    }

}
