package com.example.holing.bounded_context.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReportRequestDto(
        @Min(1) @Max(6)
        @Schema(description = "태그 id(1에서 6사이의 값)", example = "1")
        Long tagId,
        @Min(0) @Max(18)
        @Schema(description = "점수(0에서 18사이의 값)", example = "15")
        int score,
        @Schema(description = "사용자 추가 필드(사용자 추가 설문이 존재하지 않는 경우 빈 문자열)", example = "안면 홍조")
        String additional
) {

}
