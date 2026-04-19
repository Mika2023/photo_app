package com.minor.photo_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @NotBlank
    @Size(min = 1, max = 64, message = "Никнейм должен быть заполнен!")
    private String nickname;

    @NotBlank
    @Email(message = "Неверный формат почты!")
    private String email;

    @NotBlank
    @Size(min = 8, max = 64, message = "Пароль должен состоять из 8 символов и больше!")
    private String password;
}
