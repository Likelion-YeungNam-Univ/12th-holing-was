package com.example.holing.bounded_context.mission.repository;

import com.example.holing.bounded_context.mission.entity.MissionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MissionResultRepository extends JpaRepository<MissionResult, Long> {
    @Query("SELECT mr FROM MissionResult mr WHERE mr.user.id = :userId AND mr.createdAt BETWEEN :startOfDay AND :endOfDay")
    List<MissionResult> findAllByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
