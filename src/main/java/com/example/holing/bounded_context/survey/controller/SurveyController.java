package com.example.holing.bounded_context.survey.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.survey.dto.SymptomQuestionResponseDto;
import com.example.holing.bounded_context.survey.entity.SelfQuestion;
import com.example.holing.bounded_context.survey.entity.SymptomQuestion;
import com.example.holing.bounded_context.survey.service.SurveyService;
import com.example.holing.bounded_context.user.entity.Gender;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "[테스트 문항 관련 API]", description = "자가 / 증상 테스트 문항 조회 API")
@RequestMapping("/survey")
public class SurveyController {
    private final SurveyService surveyService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/symptom-test")
    @Operation(summary = "증상 테스트 문항 조회", description = "사용자가 증상 테스트의 문항을 조회하기 위한 API 입니다")
    public ResponseEntity<List<SymptomQuestionResponseDto>> readQuestion(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        List<SymptomQuestion> symptomQuestions = user.getIsPeriod() ? surveyService.readSymptomAll() : surveyService.readSymptomAllByTagNotPeriod();
        List<SymptomQuestionResponseDto> response = symptomQuestions.stream().map(SymptomQuestionResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/self-test")
    @Operation(summary = "자가 테스트 문항 조회", description = "사용자가 자가 테스트의 문항을 조회하기 위한 API 입니다")
    public ResponseEntity<Page<SelfQuestion>> readSelfQuestion(@RequestParam Gender gender,
                                                               @PageableDefault(page = 0, size = 1) Pageable pageable) {
        Page<SelfQuestion> response = surveyService.readSelfByUser(gender, pageable);
        return ResponseEntity.ok().body(response);
    }
}
