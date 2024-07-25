package com.example.holing.bounded_context.mission.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.mission.dto.MissionResultResponseDto;
import com.example.holing.bounded_context.mission.repository.MissionRepository;
import com.example.holing.bounded_context.mission.repository.MissionResultRepository;
import com.example.holing.bounded_context.mission.service.MissionResultService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionResultController {

    private final JwtProvider jwtProvider;
    private final MissionResultService missionResultService;
    private final MissionRepository missionRepository;
    private final MissionResultRepository missionResultRepository;

    /**
     * 매일 랜덤으로 미션 3개 생성하기 위한 Method
     *
     * @return List<Mission> 생성된 미션 3개
     */
//    @Scheduled(cron = "0 0 0 * * ?")
    @GetMapping("")
    ResponseEntity<List<MissionResultResponseDto>> create(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        List<MissionResultResponseDto> missionResultList = missionResultService.create(Long.parseLong(userId));

        return ResponseEntity.ok().body(missionResultList);
    }

    /**
     * 오늘 생성된 미션(3개) 조회 함수
     *
     * @param request
     * @return
     */
    @GetMapping("/today")
    ResponseEntity<List<MissionResultResponseDto>> read(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        List<MissionResultResponseDto> todayMissionResultList = missionResultService.read(Long.parseLong(userId));

        return ResponseEntity.ok().body(todayMissionResultList);
    }


    /**
     * 미션 교체 API
     *
     * @param request
     * @param missionResultId
     * @return
     */
    @PatchMapping("/{missionResultId}")
    ResponseEntity<MissionResultResponseDto> update(HttpServletRequest request, @PathVariable Long missionResultId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        MissionResultResponseDto response = missionResultService.update(Long.parseLong(userId), missionResultId);

        return ResponseEntity.ok().body(response);
    }
}
