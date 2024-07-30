package com.example.holing.bounded_context.mission.service;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.bounded_context.mission.dto.MissionCountDto;
import com.example.holing.bounded_context.mission.dto.MissionResultResponseDto;
import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.mission.entity.MissionResult;
import com.example.holing.bounded_context.mission.exception.MissionExceptionCode;
import com.example.holing.bounded_context.mission.repository.MissionRepository;
import com.example.holing.bounded_context.mission.repository.MissionResultRepository;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MissionResultService {

    private final UserService userService;
    private final MissionResultRepository missionResultRepository;
    private final MissionService missionService;
    private final MissionRepository missionRepository;

    @Transactional
    public List<MissionResultResponseDto> create(Long userId) {
        User user = userService.read(userId);

        if (checkCreated(user)) {
            throw new GlobalException(MissionExceptionCode.ALREADY_EXISTS);
        }

        user.setIsChanged(false);

        List<Mission> missions = missionService.getMissions(userId);
        List<MissionResult> todayMission = new ArrayList<>();

        for (Mission mission : missions) {
            MissionResult missionResult = MissionResult.builder().build();
            missionResult.setMission(mission);
            missionResult.setUser(user);

            todayMission.add(missionResult);
            missionResultRepository.save(missionResult);
        }

        return todayMission.stream()
                .map(MissionResultResponseDto::fromEntity)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<MissionResultResponseDto> read(Long userId, LocalDate date) {
        User user = userService.read(userId);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<MissionResult> todayMission = missionResultRepository.findAllByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);

        return todayMission.stream()
                .map(MissionResultResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public MissionResultResponseDto update(Long userId, Long missionResultId) {
        User user = userService.read(userId);

        if (checkUpdated(user)) {
            throw new GlobalException(MissionExceptionCode.ALREADY_UPDATED);
        }

//        MissionResult missionResult = missionResultRepository.findById(missionResultId).get();
        MissionResult missionResult = user.getMissionResults().stream()
                .filter(mr -> mr.getId().equals(missionResultId)).findFirst().get();

        // 지난 날짜의 미션에 대한 요청
        if (missionResult.getCreatedAt().getDayOfMonth() != LocalDateTime.now().getDayOfMonth()) {
            throw new GlobalException(MissionExceptionCode.ACCESS_DENIED);
        }

        // 이미 완료된 미션에 대한 요청
        if (missionResult.isCompleted() == true) {
            throw new GlobalException(MissionExceptionCode.ALREADY_COMPLETED);
        }

        Mission prevMission = missionResult.getMission();

        List<Mission> newMissionList = missionRepository.findAllByTag(prevMission.getTag());

        Random random = new Random();

        while (true) {
            int temp = random.nextInt(newMissionList.size());
            Mission tempMission = newMissionList.get(temp);

            if (tempMission != prevMission) {
                missionResult.setMission(tempMission);
                user.setIsChanged(true);
                break;
            }
        }

        return MissionResultResponseDto.fromEntity(missionResult);
    }

    @Transactional
    public MissionResultResponseDto complete(Long userId, Long missionResultId) {
        User user = userService.read(userId);
        MissionResult missionResult = missionResultRepository.findById(missionResultId).get();

        if (missionResult.isCompleted() == true) {
            throw new GlobalException(MissionExceptionCode.ALREADY_COMPLETED);
        }

        if (missionResult.getCreatedAt().getDayOfMonth() != LocalDateTime.now().getDayOfMonth()) {
            throw new GlobalException(MissionExceptionCode.ACCESS_DENIED);
        }

        missionResult.updateState(true);

        user.addPoint(missionResult.getMission().getReward());
        return MissionResultResponseDto.fromEntity(missionResult);
    }

    @Transactional
    public List<MissionCountDto> countCompletedMissions(Long userId, LocalDateTime startAt, LocalDateTime finishAt) {
        List<MissionResult> monthTotal = missionResultRepository.findAllByUserIdAndCreatedAtBetween(userId, startAt, finishAt);

        List<MissionResult> completedMission = monthTotal.stream()
                .filter(MissionResult::isCompleted)
                .toList();

        Map<LocalDate, Integer> countByDate = new HashMap<>();
        for (MissionResult cm : completedMission) {
            LocalDate date = cm.getCreatedAt().toLocalDate();
            countByDate.put(date, countByDate.getOrDefault(date, 0) + 1);
        }

        List<MissionCountDto> missionCountDtos = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : countByDate.entrySet()) {
            missionCountDtos.add(new MissionCountDto(entry.getKey(), entry.getValue()));
        }

        missionCountDtos.sort(Comparator.comparing(MissionCountDto::date));
        return missionCountDtos;
    }

    // 미션 생성 여부 확인 (생성되어 있으면 true, 안되어 있으면 false 반환)
    public boolean checkCreated(User user) {
        List<MissionResult> missionResults = user.getMissionResults();
        missionResults.sort((mr1, mr2) -> mr2.getCreatedAt().compareTo(mr1.getCreatedAt()));

        if (missionResults.isEmpty()) return false;
        for (int i = 0; i < 3; i++) {
            if (missionResults.get(i).getCreatedAt().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
                return true;
            }
        }

        return false;
    }

    // 미션 교체 여부 확인 (미션 교체 되었다면 true, 교체된적 없다면 false)
    public boolean checkUpdated(User user) {
        if (!checkCreated(user)) {
            throw new GlobalException(MissionExceptionCode.NOT_EXIST_MISSION);
        }

        if (user.getIsChanged() == true) {
            return true;
        }

        return false;
    }

}
