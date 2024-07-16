package com.example.atm.bounded_context.schedule.service;

import com.example.atm.bounded_context.schedule.dto.ScheduleRequestDto;
import com.example.atm.bounded_context.schedule.dto.ScheduleResponseDto;
import com.example.atm.bounded_context.schedule.entity.Schedule;
import com.example.atm.bounded_context.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * 일정 등록
     * @param 일정 dto
     */
    public ScheduleResponseDto create(ScheduleRequestDto scheduleRequestDto) {
        LocalDateTime startAt = scheduleRequestDto.startAt();
        LocalDateTime finishAt = scheduleRequestDto.finishAt();

        // 시작 날짜가 종료 날짜보다 앞선 경우 or 종료 날짜가 시작 날짜보다 앞선 경우
        if (startAt.isAfter(finishAt) || finishAt.isBefore(startAt)) {
            throw new IllegalArgumentException("잘못된 일정입니다.");
        }

        Schedule schedule = scheduleRequestDto.toEntity();
        return ScheduleResponseDto.fromEntity(schedule);
    }

    /**
     * 오늘 날짜에 등록된 일정 불러오기
     * @param today
     */
    public List<Schedule> read(LocalDate today) {
        return scheduleRepository.findByDateOrderByStartAt(today);
    }

    /**
     * 일정 수정
     * 기존의 입력사항을 유지한채 일정 항목을 모두 받아서 update
     */
    public ScheduleResponseDto update(Long scheduleId, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        schedule.update(
                scheduleRequestDto.title(),
                scheduleRequestDto.place(),
                scheduleRequestDto.startAt(),
                scheduleRequestDto.finishAt()
        );

        scheduleRepository.save(schedule);

        return ScheduleResponseDto.fromEntity(schedule);
    }

    /**
     * 일정 삭제 - 삭제 버튼을 누를 경우 삭제
     * @param scheduleId
     */
    public void delete(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

}
