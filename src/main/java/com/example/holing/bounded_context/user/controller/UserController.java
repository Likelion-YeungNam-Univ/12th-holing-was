package com.example.holing.bounded_context.user.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.user.api.UserApi;
import com.example.holing.bounded_context.user.dto.UserInfoDto;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final JwtProvider jwtProvider;
    private final UserService userService;


    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("테스트 성공");
    }


    public ResponseEntity<UserInfoDto> read(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        UserInfoDto response = UserInfoDto.fromEntity(user);

        return ResponseEntity.ok().body(response);
    }

//    /**
//     * 사용자 정보 조회 : 사용자가 다른 사용자의 정보를 조회하기 위한 메서드입니다.
//     *
//     * @param userId 대상 사용자 아이디
//     * @return UserInfoDto 사용자 정보
//     */
//    @GetMapping("/{userId}")
//    ResponseEntity<UserInfoDto> read(@PathVariable Long userId) {
//
//        User user = userService.read(userId);
//        UserInfoDto response = UserInfoDto.fromEntity(user);
//
//        return ResponseEntity.ok().body(response);
//    }

    public ResponseEntity<String> connectMate(HttpServletRequest request, @PathVariable Long socialId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        userService.connectMate(Long.parseLong(userId), socialId);

        return ResponseEntity.ok().body("짝꿍 연결에 성공했습니다.");
    }

    public ResponseEntity<String> disconnectMate(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String acceptUserId = jwtProvider.getUserId(accessToken);

        userService.disconnectMate(Long.parseLong(acceptUserId));

        return ResponseEntity.ok().body("짝꿍 해제에 성공했습니다.");
    }

}
