package com.example.holing.bounded_context.mission.entity;

import com.example.holing.bounded_context.survey.entity.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column(nullable = false)
    private int reward;

    @Column(nullable = false)
    private String description;

    @Builder
    public Mission(String title, String content, Tag tag, int reward, String description) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.reward = reward;
        this.description = description;
    }
}