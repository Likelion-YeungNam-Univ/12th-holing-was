package com.example.holing.bounded_context.missionresult.entity;

import com.example.holing.bounded_context.mission.entity.Mission;
import com.example.holing.bounded_context.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Entity
@Getter
public class MissionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MissionState state;

    @Column(nullable = false)
    @CreatedDate
    private LocalDate createAt;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDate modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    private Mission mission;

    @Builder
    public MissionResult() {
        this.state = MissionState.INCOMPLETED;
    }

    public void updateState(MissionState state) {
        this.state = state;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }
}
