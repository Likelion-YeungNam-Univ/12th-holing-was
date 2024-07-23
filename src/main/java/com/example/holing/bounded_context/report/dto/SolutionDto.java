package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.survey.entity.Solution;

public record SolutionDto(
        String title,
        String summary,
        String content1,
        String content2,
        String content3
) {
    public static SolutionDto fromEntity(Solution solution) {
        return new SolutionDto(
                solution.getTitle(),
                solution.getSummary(),
                solution.getContent1(),
                solution.getContent2(),
                solution.getContent3()
        );
    }

}
