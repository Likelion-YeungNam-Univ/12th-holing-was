package com.example.holing.bounded_context.mission.service;

import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.mission.repository.MissionRepository;
import com.example.holing.bounded_context.survey.entity.Tag;
import com.example.holing.bounded_context.survey.repository.TagRepository;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserService userService;
    private final UserReportService userReportService;
    private final TagRepository tagRepository;

    public List<Mission> generateRandomNumbers(List<Tag> tags) {
        Random random = new Random();
        tags.add(tagRepository.findById(7l).get());
        List<Mission> tagMissions = missionRepository.findByTags(tags);

        // 일상 태그의 미션은 따로 분리하여 랜덤 생성
        List<Mission> dailyMissions = tagMissions.stream()
                .filter(mission -> mission.getTag().getName().equals("DAILY"))
                .collect(Collectors.toList());

        Mission generatedDailyMission = dailyMissions.get(random.nextInt(dailyMissions.size()));

        // 랜덤 생성 된 일상 미션 추가
        List<Mission> randomMissions = new ArrayList<>();
        randomMissions.add(generatedDailyMission);

        // 증상 태그 중복 방지를 위해 Set 사용
        Set<Tag> tagSet = new HashSet<>();
        while (tagSet.size() < 2) {
            int missionIndex = random.nextInt(tagMissions.size());
            Mission tempMission = tagMissions.get(missionIndex);

            //
            if (tempMission.getTag().getName() == "DAILY") continue;

            tagSet.add(tempMission.getTag());
            randomMissions.add(tempMission);
        }

        return randomMissions;
    }

    @Transactional
    public List<Mission> getMissions(Long userId) {

        User user = userService.read(userId);
        // 짝꿍의 증상 태그를 받아옴
        List<Tag> mateTags = userReportService.getUserRecentReportTag(user.getMate());

        // 짝꿍의 증상 태그에 대한 미션 2개 + 일상 미션 1개를 받아옴
        List<Mission> missions = generateRandomNumbers(mateTags);

        return missions;
    }
}
