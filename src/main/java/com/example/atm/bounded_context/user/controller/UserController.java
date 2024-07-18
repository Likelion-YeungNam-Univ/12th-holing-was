package com.example.atm.bounded_context.user.controller;

import com.example.atm.base.jwt.JwtProvider;
import com.example.atm.bounded_context.user.dto.UserInfoDto;
import com.example.atm.bounded_context.user.entity.User;
import com.example.atm.bounded_context.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * 사용자 본인 정보 조회 : 사용자가 본인의 정보를 조회하기 위한 메서드입니다.
     *
     * @param request
     * @return UserInfoDto 사용자 정보
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
     * 사용자 정보 조회 : 사용자가 다른 사용자의 정보를 조회하기 위한 메서드입니다.
     *
     * @param userId 대상 사용자 아이디
     * @return UserInfoDto 사용자 정보
     */
    @GetMapping("/{userId}")
    ResponseEntity<UserInfoDto> read(@PathVariable Long userId) {

        User user = userService.read(userId);
        UserInfoDto response = UserInfoDto.fromEntity(user);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 짝궁 연결 : 사용자가 짝꿍을 연결하기 위한 메서드입니다.
     *
     * @param request
     * @param socialId 대상 사용자 소셜 아이디
     * @return
     * @throws IllegalArgumentException 본인의 짝꿍이 이미 존재하는 경우 발생합니다.
     * @throws IllegalArgumentException 짝꿍 대상자가 존재하지 않는 경우 발생합니다.
     * @throws IllegalArgumentException 짝꿍 대상자의 짝꿍이 이미 존재하는 경우 발생합니다.
     */
    @PatchMapping("/connect/{socialId}")
    public ResponseEntity<String> connectMate(HttpServletRequest request, @PathVariable Long socialId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        userService.connectMate(Long.parseLong(userId), socialId);

        return ResponseEntity.ok().body("짝꿍 연결에 성공했습니다.");
    }

    /**
     * 짝궁 해제 : 사용자의 짝꿍을 해제하기위한 메서드입니다.
     *
     * @param request
     * @return
     * @throws IllegalArgumentException 본인의 짝꿍이 존재하지 않는 경우 발생합니다.
     */
    @PatchMapping("/disconnect")
    public ResponseEntity<String> disconnectMate(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String acceptUserId = jwtProvider.getUserId(accessToken);

        userService.disconnectMate(Long.parseLong(acceptUserId));

        return ResponseEntity.ok().body("짝꿍 해제에 성공했습니다.");
    }

}
