package com.minor.photo_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileInfoDto {
    private Long id;
    private String avatarImageUrl;
    private String nickname;
    private String email;
}
