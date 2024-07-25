package com.example.holing.bounded_context.user.entity;

import com.example.holing.bounded_context.auth.dto.OAuthUserInfoDto;
import com.example.holing.bounded_context.auth.dto.SignInRequestDto;
import com.example.holing.bounded_context.mission.entity.MissionResult;
import com.example.holing.bounded_context.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @OneToMany(mappedBy = "user")
    List<Schedule> schedules = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImgUrl;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Boolean isPeriod;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private Long socialId;

    @OneToOne
    private User mate;

    @OneToMany(mappedBy = "user")
    private List<MissionResult> missionResults;

    @Builder
    public User(String email, String password, String nickname, String profileImgUrl, Gender gender, Boolean isPeriod, Integer point, Long socialId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.gender = gender;
        this.isPeriod = (gender == Gender.FEMALE) ? isPeriod : false;
        this.point = 0;
        this.socialId = socialId;
    }

    public static User of(OAuthUserInfoDto dto, SignInRequestDto request) {
        return User.builder()
                .email(dto.email())
                .nickname(dto.nickname())
                .profileImgUrl(dto.profileImageUrl())
                .socialId(dto.id())
                .gender(request.gender())
                .isPeriod(request.isPeriod())
                .build();
    }

    public User update(String nickname, String profileImgUrl) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        return this;
    }

    public void connectMate(User user) {
        this.mate = user;
        user.mate = this;
    }

    public void disconnectMate(User user) {
        this.mate = null;
        user.mate = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
