package com.example.holing.bounded_context.schedule.service;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.bounded_context.schedule.dto.ScheduleCountDto;
import com.example.holing.bounded_context.schedule.dto.ScheduleRequestDto;
import com.example.holing.bounded_context.schedule.dto.ScheduleResponseDto;
import com.example.holing.bounded_context.schedule.entity.Schedule;
import com.example.holing.bounded_context.schedule.exception.ScheduleExceptionCode;
import com.example.holing.bounded_context.schedule.repository.ScheduleRepository;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.exception.UserExceptionCode;
import com.example.holing.bounded_context.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserRepository userRepository;

    /**
     * 일정 등록
     *
     * @param scheduleRequestDto
     */
    public ScheduleResponseDto create(Long userId, ScheduleRequestDto scheduleRequestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.USER_NOT_FOUND));

        validate(scheduleRequestDto.startAt(), scheduleRequestDto.finishAt());
        Schedule schedule = scheduleRequestDto.toEntity();

        schedule.setUser(user);
        scheduleRepository.save(schedule);

        return ScheduleResponseDto.fromEntity(schedule);
    }

    /**
     * 오늘 날짜에 등록된 일정 불러오기
     *
     * @param startAt, finishAt
     */
    public List<ScheduleResponseDto> read(Long userId, LocalDateTime startAt, LocalDateTime finishAt) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.USER_NOT_FOUND));

        List<Schedule> mySchedules = scheduleRepository.findByUserIdAndStartAtBetweenOrderByStartAtAsc(userId, startAt, finishAt);

        // 짝꿍이 있는 경우, 짝꿍의 일정도 추가한다.
        if (user.getMate() != null) {
            List<Schedule> mateSchedules = scheduleRepository.findByUserIdAndStartAtBetweenOrderByStartAtAsc(user.getMate().getId(), startAt, finishAt);
            mySchedules.addAll(mateSchedules);
        }

        if (mySchedules.isEmpty()) {
            throw new GlobalException(ScheduleExceptionCode.SCHEDULE_NOT_FOUND);
        }
        return mySchedules.stream()
                .map(ScheduleResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ScheduleCountDto> countSchedules(Long userId, LocalDateTime startAt, LocalDateTime finishAt) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.USER_NOT_FOUND));

        List<Schedule> mySchedules = scheduleRepository.findByUserIdAndStartAtBetweenOrderByStartAtAsc(userId, startAt, finishAt);

        if (user.getMate() != null) {
            mySchedules.addAll(scheduleRepository.findByUserIdAndStartAtBetweenOrderByStartAtAsc(user.getMate().getId(), startAt, finishAt));
        }

        Map<LocalDate, Integer> dateCount = new HashMap<>();
        for (Schedule schedule : mySchedules) {
            LocalDate startDate = schedule.getStartAt().toLocalDate();
            LocalDate finishDate = schedule.getFinishAt().toLocalDate();

            for (LocalDate date = startDate; !date.isAfter(finishDate); date = date.plusDays(1)) {
                dateCount.put(date, dateCount.getOrDefault(date, 0) + 1);
            }
        }

        List<ScheduleCountDto> scheduleCountDtos = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : dateCount.entrySet()) {
            scheduleCountDtos.add(new ScheduleCountDto(entry.getKey(), entry.getValue()));
        }

        scheduleCountDtos.sort(Comparator.comparing(ScheduleCountDto::date));

        return scheduleCountDtos;
    }

    /**
     * 일정 수정
     * 기존의 입력사항을 유지한채 일정 항목을 모두 받아서 update
     */
    public ScheduleResponseDto update(Long userId, Long scheduleId, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new GlobalException(ScheduleExceptionCode.SCHEDULE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.USER_NOT_FOUND));

        validate(scheduleRequestDto.startAt(), scheduleRequestDto.finishAt());

        schedule.update(
                scheduleRequestDto.title(),
                scheduleRequestDto.content(),
                scheduleRequestDto.startAt(),
                scheduleRequestDto.finishAt()
        );

        return ScheduleResponseDto.fromEntity(schedule);
    }

    /**
     * 일정 삭제 - 삭제 버튼을 누를 경우 삭제
     *
     * @param scheduleId
     */
    public void delete(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new GlobalException(ScheduleExceptionCode.SCHEDULE_NOT_FOUND));

        scheduleRepository.deleteById(scheduleId);
    }

    public void validate(LocalDateTime startAt, LocalDateTime finishAt) {
        // 시작 날짜가 종료 날짜보다 앞선 경우 or 종료 날짜가 시작 날짜보다 앞선 경우
        if (startAt.isAfter(finishAt) || finishAt.isBefore(startAt)) {
            throw new GlobalException(ScheduleExceptionCode.INVALID_DATETIME);
        }
    }
}
