package com.example.holing.bounded_context.schedule.repository;

import com.example.holing.bounded_context.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserIdAndStartAtBetweenOrderByStartAtAsc(Long userId, LocalDateTime startAt, LocalDateTime finishAt);
}
