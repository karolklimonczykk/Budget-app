package com.example.pasir_klimonczyk_karol.repository;

import com.example.pasir_klimonczyk_karol.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByGroupId(Long groupId);
}
