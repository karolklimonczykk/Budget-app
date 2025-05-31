package com.example.pasir_klimonczyk_karol.service;

import com.example.pasir_klimonczyk_karol.dto.BalanceDTO;
import com.example.pasir_klimonczyk_karol.dto.TransactionDTO;
import com.example.pasir_klimonczyk_karol.model.Transaction;
import com.example.pasir_klimonczyk_karol.model.TransactionType;
import com.example.pasir_klimonczyk_karol.model.User;
import com.example.pasir_klimonczyk_karol.repository.TransactionRepository;
import com.example.pasir_klimonczyk_karol.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAllTransactions() {
        User user = getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));
    }

    public Transaction updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji tej transkacji");
        }

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());

        return transactionRepository.save(transaction);
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setUser(getCurrentUser());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public Transaction deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji tej transkacji");
        }

        transactionRepository.delete(transaction);
        return transaction;
    }

    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono zalogowanego użytkownika"));
    }

    public BalanceDTO getUserBalance(User user, Double days) {
        List<Transaction> userTransactions = transactionRepository.findAllByUser(user);
        if (days != null) { // Filtracja po balansu po  czasie
            LocalDateTime fromDate = LocalDateTime.now().minusMinutes((long)(days * 1440)); // przelicz na minuty
            userTransactions = userTransactions.stream()
                    .filter(t -> t.getTimestamp().isAfter(fromDate))
                    .toList();
        }
        double income = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
        if (Double.isNaN(income)) income = 0.0;
        if (Double.isNaN(expense)) expense = 0.0;
        System.out.println("Income: " + income + ", Expense: " + expense + ", Balance: " + (income - expense));

        return new BalanceDTO(income, expense, income - expense);
    }
}

