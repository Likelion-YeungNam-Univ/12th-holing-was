package com.example.holing.bounded_context.missionresult.service;

import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.mission.repository.MissionRepository;
import com.example.holing.bounded_context.mission.service.MissionService;
import com.example.holing.bounded_context.missionresult.dto.MissionResultResponseDto;
import com.example.holing.bounded_context.missionresult.entity.MissionResult;
import com.example.holing.bounded_context.missionresult.entity.MissionState;
import com.example.holing.bounded_context.missionresult.repository.MissionResultRepository;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionResultService {

    private final UserRepository userRepository;
    private final MissionResultRepository missionResultRepository;
    private final MissionService missionService;
    private final MissionRepository missionRepository;

    public List<MissionResultResponseDto> create(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> GlobalException(UserException.USER_NOT_FOUND));

        List<Mission> dailyMission = missionService.getDailyMission(userId);
        List<MissionResult> todayMission = new ArrayList<>();

        for (Mission mission : dailyMission) {
            MissionResult missionResult = MissionResult.builder().build();
            missionResult.setMission(mission);
            missionResult.setUser(user);

            user.getMissionResults().add(missionResult);
            todayMission.add(missionResult);

            missionResultRepository.save(missionResult);
        }

        return todayMission.stream()
                .map(MissionResultResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MissionResultResponseDto> read(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> GlobalException(UserException.USER_NOT_FOUND));

        List<MissionResult> todayMission = missionResultRepository.findByCreateAtAndAndUserId(LocalDate.now(), userId);

        return todayMission.stream()
                .map(MissionResultResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public MissionResultResponseDto update(Long userId, Long missionResultId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> GlobalException(UserException.USER_NOT_FOUND));

        MissionResult missionResult = missionResultRepository.findById(missionResultId).get();

        // 완료된 미션에 대해서는 새로운 미션 받을 수 없음
        if (missionResult.getState() == MissionState.COMPLETED) {
            throw new IllegalArgumentException("이미 완료된 미션입니다.");
        }

        // 동일한 태그(주요 증상)명의 미션 개수를 확인
        Mission prevMission = missionResult.getMission();
        int count = missionRepository.countByTag(prevMission.getTag());
        List<Mission> newMissionList = missionRepository.findAllByTag(prevMission.getTag());

        // 난수 생성을 위한 Random 객체 선언
        Random random = new Random();

        // 난수 생성 후 미션 다시 받기 → 중복된 경우 한번더 진행
        while (true) {
            int temp = random.nextInt() * newMissionList.size();
            Mission tempMission = newMissionList.get(temp);

            // 임시로 발급한 미션이 기존의 생성된 미션이랑 일치하지 않는 경우 업데이트 후 break
            if (tempMission != prevMission) {
                missionResult.setMission(tempMission);
                break;
            }
        }

        return MissionResultResponseDto.fromEntity(missionResult);
    }


}
