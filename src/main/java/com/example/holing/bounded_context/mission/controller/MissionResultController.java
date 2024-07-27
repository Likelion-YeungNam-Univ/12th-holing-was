package com.example.holing.bounded_context.mission.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.mission.api.MissionApi;
import com.example.holing.bounded_context.mission.dto.MissionCountDto;
import com.example.holing.bounded_context.mission.dto.MissionResultResponseDto;
import com.example.holing.bounded_context.mission.service.MissionResultService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionResultController implements MissionApi {

    private final JwtProvider jwtProvider;
    private final MissionResultService missionResultService;

    /**
     * 매일 랜덤으로 미션 3개 생성하기 위한 Method
     *
     * @return List<Mission> 생성된 미션 3개
     */
    public ResponseEntity<List<MissionResultResponseDto>> create(HttpServletRequest request) {
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
    public ResponseEntity<List<MissionResultResponseDto>> read(HttpServletRequest request, @RequestParam String date) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        LocalDate selectedDate = LocalDate.parse(date);

        List<MissionResultResponseDto> todayMissionResultList = missionResultService.read(Long.parseLong(userId), selectedDate);

        return ResponseEntity.ok().body(todayMissionResultList);
    }


    /**
     * 미션 교체 API
     *
     * @param request
     * @param missionResultId
     * @return
     */
    public ResponseEntity<MissionResultResponseDto> update(HttpServletRequest request, @PathVariable Long missionResultId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        MissionResultResponseDto response = missionResultService.update(Long.parseLong(userId), missionResultId);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 미션의 상태를 완료로 변경함.
     * 사용자는 해당 미션의 리워드만큼 포인트를 받음.
     *
     * @param request
     * @param missionResultId
     * @return
     */
    public ResponseEntity<MissionResultResponseDto> complete(HttpServletRequest request, @PathVariable Long missionResultId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        MissionResultResponseDto response = missionResultService.complete(Long.parseLong(userId), missionResultId);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 월별 완료된 미션 개수 받아오는 API
     *
     * @param request
     * @param date
     * @return
     */
    public ResponseEntity<List<MissionCountDto>> getMissionCount(HttpServletRequest request, @RequestParam LocalDate date) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        YearMonth yearMonth = YearMonth.from(date);
        LocalDateTime first = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime last = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return ResponseEntity.ok().body(missionResultService.countCompletedMissions(Long.parseLong(userId), first, last));
    }

}
