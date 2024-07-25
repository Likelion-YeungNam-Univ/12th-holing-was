package com.example.holing.bounded_context.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int minScore;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String content1;

    @Column(nullable = false)
    private String content2;

    @Column(nullable = false)
    private String content3;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Column(nullable = false)
    private Boolean isAdditional;
}
