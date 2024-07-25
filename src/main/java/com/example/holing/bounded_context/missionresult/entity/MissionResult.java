package com.example.holing.bounded_context.missionresult.entity;

import com.example.holing.base.BaseTimeEntity;
import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class MissionResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Mission mission;

    @Builder
    public MissionResult() {
        this.isCompleted = false;
    }

    public void updateState(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }
}
