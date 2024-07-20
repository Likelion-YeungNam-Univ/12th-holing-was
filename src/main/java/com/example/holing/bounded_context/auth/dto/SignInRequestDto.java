package com.example.holing.bounded_context.auth.dto;

import com.example.holing.bounded_context.user.entity.Gender;

public record SignInRequestDto(
        String accessToken,
        Gender gender,
        Boolean isPeriod
) {
}
