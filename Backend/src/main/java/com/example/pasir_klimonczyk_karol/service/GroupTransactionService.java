package com.example.pasir_klimonczyk_karol.service;

import com.example.pasir_klimonczyk_karol.dto.GroupTransactionDTO;
import com.example.pasir_klimonczyk_karol.model.*;
import com.example.pasir_klimonczyk_karol.repository.DebtRepository;
import com.example.pasir_klimonczyk_karol.repository.GroupRepository;
import com.example.pasir_klimonczyk_karol.repository.MembershipRepository;
import com.example.pasir_klimonczyk_karol.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupTransactionService {
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final DebtRepository debtRepository;
    private final TransactionRepository transactionRepository;

    public GroupTransactionService(GroupRepository groupRepository, MembershipRepository membershipRepository, DebtRepository debtRepository, TransactionRepository transactionRepository) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.debtRepository = debtRepository;
        this.transactionRepository = transactionRepository;
    }

    public void addGroupTransaction(GroupTransactionDTO dto, User currentUser) {
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy"));

        List<Membership> members = membershipRepository.findByGroupId(group.getId());
        List<Long> selectedUserIds = dto.getSelectedUserIds();

        if (selectedUserIds == null || selectedUserIds.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano żadnych użytkowników");
        }
        double amountPerUser = dto.getAmount() / selectedUserIds.size();

        for (Membership member : members) {
            User debtor = member.getUser();
            if(!debtor.getId().equals(currentUser.getId()) && selectedUserIds.contains(debtor.getId())) {
                Debt debt = new Debt();
                debt.setDebtor(debtor);
                debt.setCreditor(currentUser);
                debt.setGroup(group);
                debt.setAmount(amountPerUser);
                debt.setTitle(dto.getTitle());
                debtRepository.save(debt);
            }
        }

        // Dodanie transakcji po stworzeniu grupy dla wierzyciela
        Transaction groupExpense = new Transaction();
        groupExpense.setAmount(dto.getAmount());
        groupExpense.setType(TransactionType.EXPENSE);
        groupExpense.setTags(dto.getTitle());
        groupExpense.setNotes("Wydatek grupowy: " + dto.getTitle());
        groupExpense.setUser(currentUser);
        groupExpense.setTimestamp(LocalDateTime.now());

        transactionRepository.save(groupExpense);
    }
}

