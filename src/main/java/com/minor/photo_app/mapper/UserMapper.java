package com.minor.photo_app.mapper;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.UserEditRequest;
import com.minor.photo_app.dto.request.UserRegistrationRequest;
import com.minor.photo_app.dto.response.UserProfileInfoDto;
import com.minor.photo_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntityFromRegistration(UserRegistrationRequest request);

    UserPrincipal toUserPrincipal(User user);

    UserProfileInfoDto toUserProfileInfoDto(User user);

    void updateUser(@MappingTarget User user, UserEditRequest userEditRequest);
}
