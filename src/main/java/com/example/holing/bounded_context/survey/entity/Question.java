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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String statement;

    @Column(nullable = false)
    private String choice1;

    @Column(nullable = false)
    private String choice2;

    @Column(nullable = false)
    private String choice3;

    @Column(nullable = false)
    private String choice4;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;
}
