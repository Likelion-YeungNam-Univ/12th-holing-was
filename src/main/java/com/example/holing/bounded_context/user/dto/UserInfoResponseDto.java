package com.example.holing.bounded_context.user.dto;

import com.example.holing.bounded_context.user.entity.Gender;
import com.example.holing.bounded_context.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

@Schema(description = "User 정보 응답 DTO")
public record UserInfoResponseDto(
        @Schema(description = "유저 ID", example = "1")
        Long id,
        @Schema(description = "유저 이메일", example = "user@example.com")
        String email,
        @Schema(description = "유저 닉네임", example = "nickname")
        String nickname,
        @Schema(description = "프로필 이미지 URL", example = "http://example.com/image.jpg")
        String profileImgUrl,
        @Schema(description = "유저 성별", example = "MALE")
        Gender gender,
        @Schema(description = "월경 정보", example = "false")
        Boolean isPeriod,
        @Schema(description = "유저 포인트", example = "100")
        int point,
        @Schema(description = "소셜 ID", example = "1234567890")
        Long socialId,
        @Schema(description = "짝꿍 닉네임", example = "mateNickname")
        String mateNickname
) {
    public static UserInfoResponseDto fromEntity(User user) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImgUrl(),
                user.getGender(),
                user.getIsPeriod(),
                user.getPoint(),
                user.getSocialId(),
                Optional.ofNullable(user.getMate())
                        .map(User::getNickname)
                        .orElse(null)
        );
    }
}
