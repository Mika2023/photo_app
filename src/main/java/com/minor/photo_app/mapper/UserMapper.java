package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.UserRegistrationRequest;
import com.minor.photo_app.dto.response.UserProfileInfoDto;
import com.minor.photo_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntityFromRegistration(UserRegistrationRequest request);

    UserPrincipal toUserPrincipal(User user);

    UserProfileInfoDto toUserProfileInfoDto(User user);
}
