package com.example.atm.bounded_context.user.service;

import com.example.atm.bounded_context.user.entity.User;
import com.example.atm.bounded_context.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User read(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + ":사용자가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public User read(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email + ":사용자가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public User findBySocialId(Long socialId) {
        return userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException(socialId + ":사용자가 존재하지 않습니다."));
    }

    @Transactional
    public User saveOrUpdate(User user) {
        return userRepository.findByEmail(user.getEmail())
                .map(entity -> entity.update(user.getNickname(), user.getProfileImgUrl(), user.getGender()))
                .orElseGet(() -> userRepository.save(user));
    }

    @Transactional
    public void connectMate(Long userId, Long socialId) {
        User user = read(userId);
        if (user.getMate() != null)
            throw new IllegalArgumentException("본인의 짝꿍이 이미 존재합니다.");

        User mate = findBySocialId(socialId);
        if (mate.getMate() != null)
            throw new IllegalArgumentException("대상자 짝꿍이 이미 존재합니다.");
        user.connectMate(findBySocialId(socialId));
    }

    @Transactional
    public void disconnectMate(Long userId) {
        User user = read(userId);
        user.disconnectMate(read(user.getMate().getId()));
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }
}
