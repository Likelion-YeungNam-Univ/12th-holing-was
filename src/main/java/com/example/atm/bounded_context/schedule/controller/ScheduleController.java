package com.example.atm.bounded_context.schedule.controller;

import com.example.atm.bounded_context.schedule.dto.ScheduleRequestDto;
import com.example.atm.bounded_context.schedule.dto.ScheduleResponseDto;
import com.example.atm.bounded_context.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/calendar")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 특정 날짜의 일정을 조회
     *
     * @param date
     * @return 해당 날짜의 모든 일정
     */
    @GetMapping("")
    public ResponseEntity<?> getDateSchedule(@RequestParam("user") Long userId, @RequestParam("date") String date) {
        try {
            LocalDate selectedDate = LocalDate.parse(date);
            return new ResponseEntity<>(scheduleService.read(userId, selectedDate.atStartOfDay(), selectedDate.atStartOfDay().plusDays(1)), HttpStatus.OK);
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
    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        try {
            ScheduleResponseDto scheduleResponseDto = scheduleService.create(scheduleRequestDto);
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
    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        try {
            return ResponseEntity.ok(scheduleService.update(scheduleId, scheduleRequestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 일정 삭제
     *
     * @param scheduleId
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.ok("일정 삭제 완료");
    }
}
