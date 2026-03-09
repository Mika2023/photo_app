package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.response.UserProfileInfoDto;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.mapper.UserMapper;
import com.minor.photo_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserByPrincipal(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new AuthorizationDeniedException("Пользователь не найден по токену");
        }

        Long userId = userPrincipal.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в системе"));

        return user;
    }

    public void existsByIdOrElseThrow(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public UserProfileInfoDto getProfileInfo(UserPrincipal userPrincipal) {
        User user = getUserByPrincipal(userPrincipal);
        return userMapper.toUserProfileInfoDto(user);
    }
}
