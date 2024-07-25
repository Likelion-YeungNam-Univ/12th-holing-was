package com.example.holing.bounded_context.survey.entity;

import com.example.holing.bounded_context.mission.entity.Mission;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<Solution> solutions = new ArrayList<>();

    @OneToMany(mappedBy = "tag")
    private List<Mission> missions = new ArrayList<>();
}
