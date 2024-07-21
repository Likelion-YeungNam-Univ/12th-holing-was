package com.example.holing.bounded_context.mission.service;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.mission.repository.MissionRepository;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.exception.UserExceptionCode;
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
        int totalMission = missionRepository.countAllBy();

        while (missionNumbers.size() < 3) {
            Long number = random.nextLong(totalMission);
            missionNumbers.add(number);
        }

        return missionNumbers;
    }

    @Transactional
    public List<Mission> getDailyMission(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.USER_NOT_FOUND));

        List<Mission> dailyMission = new ArrayList<>();
        Set<Long> missionNumbers = generateRandomNumbers();
        System.out.println("생성된 미션 id" + missionNumbers);

        for (Long id : missionNumbers) {
            Mission mission = missionRepository.findById(id).get();
            dailyMission.add(mission);
            missionRepository.save(mission);
        }

        return dailyMission;
    }
}
