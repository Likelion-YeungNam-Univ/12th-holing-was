package com.example.holing.bounded_context.survey.service;

import com.example.holing.bounded_context.survey.entity.SymptomQuestion;
import com.example.holing.bounded_context.survey.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<SymptomQuestion> readAll() {
        return questionRepository.findAll();
    }

    public List<SymptomQuestion> readAllByTagNotPeriod() {
        return questionRepository.findAllByTagNotPeriod();
    }
}
