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
import java.util.*;

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
    public List<MissionResultResponseDto> read(Long userId) {
        User user = userService.read(userId);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<MissionResult> todayMission = missionResultRepository.findAllByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);
//        List<MissionResult> todayMission = user.getMissionResults().stream()
//                .filter(mr -> mr.getCreatedAt().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
//                .toList();

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

        // 동일한 태그(주요 증상)명의 미션 개수를 확인
        Mission prevMission = missionResult.getMission();

        List<Mission> newMissionList = missionRepository.findAllByTag(prevMission.getTag());

        // 난수 생성을 위한 Random 객체 선언
        Random random = new Random();

        // 난수 생성 후 미션 다시 받기 → 중복된 경우 한번더 진행
        while (true) {
            int temp = random.nextInt(newMissionList.size());
            Mission tempMission = newMissionList.get(temp);

            // 임시로 발급한 미션이 기존의 생성된 미션이랑 일치하지 않는 경우 업데이트 후 break
            if (tempMission != prevMission) {
                missionResult.setMission(tempMission);
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
        List<MissionResult> missionResults = user.getMissionResults();
        missionResults.sort((mr1, mr2) -> mr2.getCreatedAt().compareTo(mr1.getCreatedAt()));

        if (!checkCreated(user)) {
            throw new GlobalException(MissionExceptionCode.UPDATED_DENIED);
        }

        for (int i = 0; i < 3; i++) {
            MissionResult checkMissionResult = missionResults.get(i);
            // 생성 시간과 수정 시간이 다르다면 -> 미션 교체한 것
            if (!checkMissionResult.getCreatedAt().isEqual(checkMissionResult.getModifiedAt())) {
                return true;
            }
        }

        return false;
    }

}
