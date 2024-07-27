package com.example.holing.bounded_context.mission.service;

import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.mission.repository.MissionRepository;
import com.example.holing.bounded_context.report.service.UserReportService;
import com.example.holing.bounded_context.survey.entity.Tag;
import com.example.holing.bounded_context.survey.repository.TagRepository;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserService userService;
    private final UserReportService userReportService;
    private final TagRepository tagRepository;

    public List<Mission> generateRandomNumbers(List<Tag> tags) {

        Tag dailyTag = tagRepository.findById(7L).get();
        tags.add(dailyTag);

        Random random = new Random();
        List<Mission> tagMissions = missionRepository.findByTagIn(tags);

        // 일상 태그의 미션은 따로 분리하여 랜덤 생성
        List<Mission> dailyMissions = tagMissions.stream()
                .filter(mission -> mission.getTag().getName().equals(dailyTag.getName()))
                .collect(Collectors.toList());


        // 랜덤 생성 된 일상 미션 추가
        List<Mission> randomMissions = new ArrayList<>();
        randomMissions.add(dailyMissions.get(random.nextInt(dailyMissions.size())));

        // 증상 태그 중복 방지를 위해 Set 사용
        Set<Tag> tagSet = new HashSet<>();
        while (tagSet.size() < 3 && randomMissions.size() < 3) {
            int missionIndex = random.nextInt(tagMissions.size());
            Mission tempMission = tagMissions.get(missionIndex);

            // DAILY 미션인 경우, 추가하지 않고 다시 진행함
            if (tempMission.getTag().getName().equals("DAILY")) continue;

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

        List<Tag> allTags = new ArrayList<>();
        allTags.addAll(mateTags);

        // 짝꿍의 증상 태그에 대한 미션 2개 + 일상 미션 1개를 받아옴
        List<Mission> missions = generateRandomNumbers(allTags);

        return missions;
    }
}
