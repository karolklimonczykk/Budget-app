package com.example.pasir_klimonczyk_karol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupTransactionDTO {
    private Long groupId; //ID grupy
    private Double amount; // Kwota (+ = zarobek, - = wydatek)
    private String type;   // INCOME lub EXPENSE
    private String title;
}
