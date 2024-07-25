package com.example.holing.bounded_context.report.entity;

import com.example.holing.bounded_context.survey.entity.Solution;
import com.example.holing.bounded_context.survey.entity.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", nullable = false)
    private Solution solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_report_id", nullable = false)
    private UserReport userReport;

    @Column(nullable = false)
    private int score;

    private String additional;

    @Builder
    public Report(Long id, Solution solution, Tag tag, UserReport userReport, int score, String additional) {
        this.id = id;
        this.solution = solution;
        this.tag = tag;
        this.userReport = userReport;
        this.score = score;
        this.additional = additional;
    }
}
