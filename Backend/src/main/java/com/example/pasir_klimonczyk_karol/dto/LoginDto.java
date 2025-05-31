package com.example.pasir_klimonczyk_karol.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @Email(message = "Niepoprawny format adresu email")
    @NotBlank(message = "Email nie może być pusty")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;
}
