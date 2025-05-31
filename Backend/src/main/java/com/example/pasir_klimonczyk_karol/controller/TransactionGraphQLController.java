package com.example.pasir_klimonczyk_karol.controller;

import com.example.pasir_klimonczyk_karol.dto.BalanceDTO;
import com.example.pasir_klimonczyk_karol.dto.TransactionDTO;
import com.example.pasir_klimonczyk_karol.model.Transaction;
import com.example.pasir_klimonczyk_karol.model.User;
import com.example.pasir_klimonczyk_karol.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
public class TransactionGraphQLController {
    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @QueryMapping
    public BalanceDTO userBalance(@Argument("days") Double days) { //  Filtracja balansu wg. czasu
        User user = transactionService.getCurrentUser();
        return transactionService.getUserBalance(user, days);
    }

    @MutationMapping
    public Transaction addTransaction(@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @MutationMapping
    public Transaction updateTransaction(
            @Argument Long id,
            @Valid @Argument TransactionDTO transactionDTO
    ) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    @MutationMapping
    public boolean deleteTransaction(@Argument Long id) {
        transactionService.deleteTransaction(id);
        return true;
    }
}
