package com.example.pasir_klimonczyk_karol.controller;

import com.example.pasir_klimonczyk_karol.dto.DebtDTO;
import com.example.pasir_klimonczyk_karol.model.Debt;
import com.example.pasir_klimonczyk_karol.service.DebtService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
@Controller
public class DebtGraphQLController {
    private final DebtService debtService;

    public DebtGraphQLController(DebtService debtService) {
        this.debtService = debtService;
    }

    @QueryMapping
    public List<Debt> groupDebts(@Argument Long groupId) {
        return debtService.getGroupDebts(groupId).stream()
                .peek(debt -> {
                    if (debt.getTitle() == null ) {
                        debt.setTitle("Brak opisu");
                    }
                })
                .toList();
    }

    @MutationMapping
    public Debt createDebt(@Valid @Argument DebtDTO debtDTO){
        return debtService.createDebt(debtDTO);
    }

}
