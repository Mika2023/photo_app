package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequest {

    @Size(min = 1, max = 64)
    private String nickname;

    @Email
    private String email;
}
