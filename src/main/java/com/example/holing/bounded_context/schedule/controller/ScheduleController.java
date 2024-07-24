package com.example.holing.bounded_context.schedule.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.schedule.api.ScheduleApi;
import com.example.holing.bounded_context.schedule.dto.ScheduleCountDto;
import com.example.holing.bounded_context.schedule.dto.ScheduleRequestDto;
import com.example.holing.bounded_context.schedule.dto.ScheduleResponseDto;
import com.example.holing.bounded_context.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class ScheduleController implements ScheduleApi {

    private final ScheduleService scheduleService;

    private final JwtProvider jwtProvider;

    /**
     * 특정 날짜의 일정을 조회
     *
     * @param date
     * @return 해당 날짜의 모든 일정
     */
    public ResponseEntity<List<ScheduleResponseDto>> getDateSchedule(HttpServletRequest request, @RequestParam("date") String date) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        LocalDate selectedDate = LocalDate.parse(date);
        return ResponseEntity.ok().body(scheduleService.read(Long.parseLong(userId), selectedDate.atStartOfDay(), selectedDate.atStartOfDay().plusDays(1)));
    }

    /**
     * 해당 월의 모든 날짜에 일정 개수를 반환함
     */

    public ResponseEntity<List<ScheduleCountDto>> getScheduleCount(HttpServletRequest request, @RequestParam("date") String date) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        LocalDate selectedDate = LocalDate.parse(date);

        YearMonth yearMonth = YearMonth.from(selectedDate);
        LocalDateTime firstDay = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime lastDay = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return ResponseEntity.ok().body(scheduleService.countSchedules(Long.parseLong(userId), firstDay, lastDay));
    }


    /**
     * 일정 등록
     *
     * @param scheduleRequestDto
     * @return
     */
    public ResponseEntity<ScheduleResponseDto> createSchedule(HttpServletRequest request, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        ScheduleResponseDto scheduleResponseDto = scheduleService.create(Long.parseLong(userId), scheduleRequestDto);

        return ResponseEntity.ok().body(scheduleResponseDto);
    }

    /**
     * 일정 수정
     *
     * @param scheduleId
     * @param scheduleRequestDto - 수정된 일정 사항
     */
    public ResponseEntity<ScheduleResponseDto> updateSchedule(HttpServletRequest request, @PathVariable Long scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        return ResponseEntity.ok().body(scheduleService.update(Long.parseLong(userId), scheduleId, scheduleRequestDto));
    }

    /**
     * 일정 삭제
     *
     * @param scheduleId
     */
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.ok("일정 삭제 완료");
    }
}
