package com.example.holing.bounded_context.report.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReportRequestDto(
        @Min(1) @Max(6)
        Long tagId,
        @Min(0) @Max(18)
        int score,
        String content
) {

}
