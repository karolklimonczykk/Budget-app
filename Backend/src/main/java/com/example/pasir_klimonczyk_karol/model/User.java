package com.example.pasir_klimonczyk_karol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // Informuje Springa, że klasa jest encją mapowaną na tabelę w bazie danych
@Table(name = "users") // Ustalamy nazwę tabeli - unikamy np. "user", które jest włowem zastrzeżonym w niektórych DB
public class User {

    @Id // pole 'id' to klucz główny
    @GeneratedValue(strategy = GenerationType.IDENTITY) // wartość id będzie generowana automatycznie przez bazę
    private Long id;

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    private String username;

    @Email(message = "Podaj poprawny ades e-mail")
    @NotBlank(message = "Email jest wymagany")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;

    private String currency = "PLN"; // domyślna waluta - do wstępnych ustawień konta
}
