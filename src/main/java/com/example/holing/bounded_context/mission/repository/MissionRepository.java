package com.example.holing.bounded_context.mission.repository;

import com.example.holing.bounded_context.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    int countAllBy();

    int countAllByTag(String tag);

    List<Mission> findAllByTag(String tag);
}
