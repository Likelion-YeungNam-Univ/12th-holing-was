package com.example.atm.bounded_context.schedule.controller;

import com.example.atm.bounded_context.schedule.entity.Schedule;
import com.example.atm.bounded_context.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/calendar")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 특정 날짜의 일정을 조회
     * @param date
     * @return 해당 날짜의 모든 일정
     */
    @GetMapping
    public ResponseEntity<List<Schedule>> getDateSchedule(@RequestParam("date") String date) {
        LocalDate selectedDate = LocalDate.parse(date);
        return new ResponseEntity<>(scheduleService.read(selectedDate), HttpStatus.OK);
    }
}
