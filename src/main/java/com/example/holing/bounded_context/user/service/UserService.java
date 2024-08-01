package com.example.holing.bounded_context.user.service;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.bounded_context.user.dto.UserExchangeRequestDto;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.exception.UserExceptionCode;
import com.example.holing.bounded_context.user.repository.UserRepository;
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
                .orElseThrow(() -> new GlobalException(UserExceptionCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User readBySocialId(Long socialId) {
        return userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.TARGET_NOT_FOUND));
    }

    @Transactional
    public User saveOrUpdate(User user) {
        return userRepository.findByEmail(user.getEmail())
                .map(entity -> entity.update(
                        user.getNickname(),
                        user.getProfileImgUrl()))
                .orElseGet(() -> userRepository.save(user));
    }

    @Transactional
    public void connectMate(Long userId, Long socialId) {
        User user = read(userId);
        if (user.getMate() != null)
            throw new GlobalException(UserExceptionCode.USER_MATE_EXISTS);

        User mate = readBySocialId(socialId);
        if (mate.getMate() != null)
            throw new GlobalException(UserExceptionCode.TARGET_MATE_EXISTS);
        user.connectMate(readBySocialId(socialId));
    }

    @Transactional
    public void disconnectMate(Long userId) {
        User user = read(userId);
        if (user.getMate() == null)
            throw new GlobalException(UserExceptionCode.MATE_NOT_FOUND);
        user.disconnectMate(read(user.getMate().getId()));
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public User exchange(Long userId, UserExchangeRequestDto userExchangeRequestDto) {
        User user = read(userId);

        int productPrice = userExchangeRequestDto.getProductPrice();
        if (user.getPoint() < productPrice) {
            throw new GlobalException(UserExceptionCode.LACK_OF_POINT);
        } else {
            user.addPoint(-productPrice);
        }

        return user;
    }

    @Transactional
    public User completeSelfTest(Long userId) {
        User user = read(userId);
        user.setIsSelfTested(true);

        return user;
    }
}
