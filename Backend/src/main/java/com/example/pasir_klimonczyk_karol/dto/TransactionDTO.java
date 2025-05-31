package com.example.pasir_klimonczyk_karol.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TransactionDTO {

    @Setter
    @NotNull(message = "Kwota nie może być pusta")
    @Min(value = 1, message = "Kwota musi być większa niż 0")
    private Double amount; // Kwota transakcji

    @NotNull(message = "Typ transakcji jest wymagany")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Typ transakcji musi być INCOME lub EXPENSE")
    private String type; // Typ transakcji (INCOME lub EXPENSE)

    @Size(max = 50, message = "Tagi nie mogą przekraczać 50 znaków")
    private String tags; // Lista tagów jako String

    @Size(max = 255, message = "Notatka może mieć maksymalnie 255 znaków")
    private String notes; // Dodatkowe notatki
}
