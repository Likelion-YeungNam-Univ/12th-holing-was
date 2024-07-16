package com.example.atm.bounded_context.schedule.repository;

import com.example.atm.bounded_context.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<List<Schedule>> findByDateBetween(LocalDate startDate, LocalDate finishDate);

    List<Schedule> findByDateOrderByStartAt(LocalDate today);
}
