package com.example.holing.bounded_context.missionresult.repository;

import com.example.holing.bounded_context.missionresult.entity.MissionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MissionResultRepository extends JpaRepository<MissionResult, Long> {
    List<MissionResult> findByCreateAtAndAndUserId(LocalDate createAt, Long userId);
}
