package com.example.holing.bounded_context.survey.dto;

import com.example.holing.bounded_context.survey.entity.Tag;

public record TagDto(
        Long id,
        String tagName,
        String manImgUrl,
        String womanImgUrl
) {
    public static TagDto FromEntity(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getName(),
                tag.getManImgUrl(),
                tag.getWomanImgUrl()
        );
    }
}
