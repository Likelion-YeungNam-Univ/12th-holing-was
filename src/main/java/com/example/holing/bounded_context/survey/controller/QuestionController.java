package com.example.holing.bounded_context.survey.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.survey.dto.SymptomQuestionResponseDto;
import com.example.holing.bounded_context.survey.entity.SymptomQuestion;
import com.example.holing.bounded_context.survey.service.QuestionService;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/symptom")
    @Operation(summary = "증상 테스트 문항 조회", description = "사용자가 자가테스트의 문항을 조회하기 위한 API 입니다")
    public ResponseEntity<List<SymptomQuestionResponseDto>> readQuestion(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        List<SymptomQuestion> symptomQuestions = user.getIsPeriod() ? questionService.readAll() : questionService.readAllByTagNotPeriod();
        List<SymptomQuestionResponseDto> response = symptomQuestions.stream().map(SymptomQuestionResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }
}
