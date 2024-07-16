package com.example.atm.bounded_context.user.dto;

import com.example.atm.bounded_context.user.entity.Gender;
import com.example.atm.bounded_context.user.entity.User;

public record UserInfoDto(
        Long id,
        String email,
        String nickname,
        String profileImgUrl,
        Gender gender,
        Integer point
) {
    public static UserInfoDto fromEntity(User user) {
        return new UserInfoDto(
                user.getId(), user.getEmail(), user.getNickname(), user.getProfileImgUrl(), user.getGender(), user.getPoint());
    }
}
