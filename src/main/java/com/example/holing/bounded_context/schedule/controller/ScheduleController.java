package com.example.holing.bounded_context.schedule.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.schedule.dto.ScheduleRequestDto;
import com.example.holing.bounded_context.schedule.dto.ScheduleResponseDto;
import com.example.holing.bounded_context.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    private final JwtProvider jwtProvider;

    /**
     * 특정 날짜의 일정을 조회
     *
     * @param date
     * @return 해당 날짜의 모든 일정
     */
    @GetMapping("/schedules")
    public ResponseEntity<?> getDateSchedule(HttpServletRequest request, @RequestParam("date") String date) {
        try {
            String accessToken = jwtProvider.getToken(request);
            String userId = jwtProvider.getUserId(accessToken);

            LocalDate selectedDate = LocalDate.parse(date);
            return new ResponseEntity<>(scheduleService.read(Long.parseLong(userId), selectedDate.atStartOfDay(), selectedDate.atStartOfDay().plusDays(1)), HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 일정 등록
     *
     * @param scheduleRequestDto
     * @return
     */
    @PostMapping("/schedules")
    public ResponseEntity<?> createSchedule(HttpServletRequest request, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        try {
            String accessToken = jwtProvider.getToken(request);
            String userId = jwtProvider.getUserId(accessToken);

            ScheduleResponseDto scheduleResponseDto = scheduleService.create(Long.parseLong(userId), scheduleRequestDto);

            return ResponseEntity.ok(scheduleResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 일정 수정
     *
     * @param scheduleId
     * @param scheduleRequestDto - 수정된 일정 사항
     */
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<?> updateSchedule(HttpServletRequest request, @PathVariable Long scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        try {
            String accessToken = jwtProvider.getToken(request);
            String userId = jwtProvider.getUserId(accessToken);

            return ResponseEntity.ok(scheduleService.update(Long.parseLong(userId), scheduleId, scheduleRequestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 일정 삭제
     *
     * @param scheduleId
     */
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.ok("일정 삭제 완료");
    }
}
