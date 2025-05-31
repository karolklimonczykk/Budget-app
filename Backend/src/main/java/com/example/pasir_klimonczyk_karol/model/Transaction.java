package com.example.pasir_klimonczyk_karol.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Encja Transaction reprezentuje pojedyncza transakcje finansowa.
 * Każda transakcja ma unikalny identyfikator, kwotę, typ, tagi, notatki oraz datę utworzenia.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "transactions") // Należy użyć "transactions" zamiast "transaction",
                                //ponieważ "transaction" jest zarezerwowanym słowem w SQL.
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount; // Kwota transakcji

    @Enumerated(EnumType.STRING)
    private TransactionType type; // Typ transakcji (INCOME lub EXPENSE)

    private String tags; // Lista tagów lub pojedynczy tag (dla uproszczenia jako String)

    private String notes; // Dodatkowe notatki

    private LocalDateTime timestamp; // Data i czas utworzenia transakcji

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Transaction(Double amount, TransactionType type, String tags, String notes,User user) {
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.notes = notes;
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }
}
