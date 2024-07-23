package com.example.holing.bounded_context.schedule.entity;

import com.example.holing.bounded_context.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @ManyToOne
    User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String content;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime finishAt;

    @Builder
    public Schedule(String title, String content, LocalDateTime startAt, LocalDateTime finishAt) {
        this.title = title;
        this.content = content;
        this.startAt = startAt;
        this.finishAt = finishAt;
    }

    public void update(String title, String content, LocalDateTime startAt, LocalDateTime finishAt) {
        this.title = title;
        this.content = content;
        this.startAt = startAt;
        this.finishAt = finishAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

}