package com.example.holing.bounded_context.auth.dto;

import com.example.holing.bounded_context.user.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequestDto(
        @Schema(description = "유저 성별[최초가입시 필수]", example = "MALE")
        Gender gender,
        @Schema(description = "월경 정보[최초가입시 필수]", example = "false")
        Boolean isPeriod
) {
}
