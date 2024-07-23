package com.example.holing.bounded_context.survey.dto;

import com.example.holing.bounded_context.survey.entity.Question;

public record QuestionResponseDto(
        Long id,
        String statement,
        String choice1,
        String choice2,
        String choice3,
        String tagName
) {
    public static QuestionResponseDto fromEntity(Question question) {
        return new QuestionResponseDto(
                question.getId(),
                question.getStatement(),
                question.getChoice1(),
                question.getChoice2(),
                question.getChoice3(),
                question.getTag().getName()
        );
    }
}
