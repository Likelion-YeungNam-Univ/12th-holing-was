package com.example.holing.bounded_context.survey.service;

import com.example.holing.bounded_context.survey.entity.SelfQuestion;
import com.example.holing.bounded_context.survey.entity.SymptomQuestion;
import com.example.holing.bounded_context.survey.entity.Type;
import com.example.holing.bounded_context.survey.repository.SelfQuestionRepository;
import com.example.holing.bounded_context.survey.repository.SymptomQuestionRepository;
import com.example.holing.bounded_context.user.entity.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SymptomQuestionRepository symptomQuestionRepository;
    private final SelfQuestionRepository selfQuestionRepository;

    public List<SymptomQuestion> readSymptomAll() {
        return symptomQuestionRepository.findAll();
    }

    public List<SymptomQuestion> readSymptomAllByTagNotPeriod() {
        return symptomQuestionRepository.findAllByTagNotPeriod();
    }

    public Page<SelfQuestion> readSelfByUser(Gender gender, Pageable pageable) {
        return selfQuestionRepository.findAllByTypeNot(
                gender == Gender.MALE ? Type.FEMALE : Type.MALE,
                pageable);
    }
}
