package com.example.atm.bounded_context.user.service;

import com.example.atm.bounded_context.user.entity.User;
import com.example.atm.bounded_context.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    @Transactional
    public User saveOrUpdate(User user) {
        return userRepository.findByEmail(user.getEmail())
                .map(entity -> entity.update(user.getNickname(), user.getProfileImgUrl(), user.getGender()))
                .orElseGet(() -> userRepository.save(user));
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }
}
