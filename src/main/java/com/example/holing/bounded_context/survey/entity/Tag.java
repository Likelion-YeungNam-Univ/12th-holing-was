package com.example.holing.bounded_context.survey.entity;

import com.example.holing.bounded_context.mission.entity.Mission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String manImgUrl;

    @Column(nullable = true)
    private String womanImgUrl;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<Solution> solutions = new ArrayList<>();

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<Mission> missions = new ArrayList<>();
}
