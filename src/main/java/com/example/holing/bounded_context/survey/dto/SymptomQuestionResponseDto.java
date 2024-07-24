package com.example.holing.bounded_context.survey.dto;

import com.example.holing.bounded_context.survey.entity.SymptomQuestion;

public record SymptomQuestionResponseDto(
        Long id,
        String statement,
        String choice1,
        String choice2,
        String choice3,
        String choice4,
        String tagName,
        Boolean isAdditional
) {
    public static SymptomQuestionResponseDto fromEntity(SymptomQuestion symptomQuestion) {
        return new SymptomQuestionResponseDto(
                symptomQuestion.getId(),
                symptomQuestion.getStatement(),
                symptomQuestion.getChoice1(),
                symptomQuestion.getChoice2(),
                symptomQuestion.getChoice3(),
                symptomQuestion.getChoice4(),
                symptomQuestion.getTag().getName(),
                symptomQuestion.getIsAdditional()
        );
    }
}
