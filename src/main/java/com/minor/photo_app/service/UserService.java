package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.UserEditRequest;
import com.minor.photo_app.dto.response.UserProfileInfoDto;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.mapper.UserMapper;
import com.minor.photo_app.repository.UserRepository;
import com.minor.photo_app.service.fileStorage.factory.FileStorageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileStorageFactory fileStorageFactory;

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

    public UserProfileInfoDto editProfileInfo(
            UserPrincipal userPrincipal, UserEditRequest userEditRequest, MultipartFile profileImage
    ) {
        if (userEditRequest == null && profileImage == null) {
            throw new IllegalArgumentException("Нет данных на изменение информации о профиле");
        }

        User user = getUserByPrincipal(userPrincipal);
        userMapper.updateUser(user, userEditRequest);
        if (profileImage != null) {
            String imageUrl = fileStorageFactory.getFileStorageToSave().saveFile(profileImage);
            user.setAvatarImageUrl(imageUrl);
        }
        userRepository.save(user);
        return userMapper.toUserProfileInfoDto(user);
    }

    public Long getIdByEmailOrElseThrow(String email) {
        return userRepository.findIdByEmail(email)
                .orElseThrow(() -> new NotFoundException("Аккаунт с такой почтой не найден"));
    }
}
