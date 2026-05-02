package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordRequest {
    @NotNull
    private Long userId;

    @NotBlank
    @Size(min = 8, max = 64, message = "Пароль должен состоять из 8 символов и больше!")
    private String newPassword;
}
