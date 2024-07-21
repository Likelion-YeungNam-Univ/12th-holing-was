package com.example.holing.bounded_context.mission.service;

import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.mission.repository.MissionRepository;
import com.example.holing.bounded_context.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    Set<Long> generateRandomNumbers() {
        Random random = new Random();
        Set<Long> missionNumbers = new HashSet<>();
        int totalMission = missionRepository.countById();

        while (missionNumbers.size() < 3) {
            Long number = random.nextLong(totalMission);
            missionNumbers.add(number);
        }

        return missionNumbers;
    }

    @Transactional
    public List<Mission> getDailyMission(Long userId) {

        List<Mission> dailyMission = new ArrayList<>();
        Set<Long> missionNumbers = generateRandomNumbers();

        for (Long id : missionNumbers) {
            Mission mission = missionRepository.findById(id).get();
            dailyMission.add(mission);
            missionRepository.save(mission);
        }

        return dailyMission;
    }
}