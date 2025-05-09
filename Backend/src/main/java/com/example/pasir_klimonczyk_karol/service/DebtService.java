package com.example.pasir_klimonczyk_karol.service;

import com.example.pasir_klimonczyk_karol.dto.DebtDTO;
import com.example.pasir_klimonczyk_karol.model.Debt;
import com.example.pasir_klimonczyk_karol.model.Group;
import com.example.pasir_klimonczyk_karol.model.User;
import com.example.pasir_klimonczyk_karol.repository.DebtRepository;
import com.example.pasir_klimonczyk_karol.repository.GroupRepository;
import com.example.pasir_klimonczyk_karol.repository.MembershipRepository;
import com.example.pasir_klimonczyk_karol.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {

    private final DebtRepository debtRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public DebtService(DebtRepository debtRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.debtRepository = debtRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<Debt> getGroupDebts(Long groupId) {
        return debtRepository.findByGroupId(groupId);
    }

    public Debt createDebt(DebtDTO debtDTO) {
        Group group = groupRepository.findById(debtDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy o ID: " + debtDTO.getGroupId()));
        User debtor = userRepository.findById(debtDTO.getDebtorId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono dłużnika o ID: " + debtDTO.getDebtorId()));
        User creditor = userRepository.findById(debtDTO.getCreditorId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono wierzyciela o ID: " + debtDTO.getCreditorId()));
        Debt debt = new Debt();
        debt.setGroup(group);
        debt.setCreditor(creditor);
        debt.setDebtor(debtor);
        debt.setAmount(debtDTO.getAmount());
        debt.setTitle(debtDTO.getTitle());

        return debtRepository.save(debt);
    }
}
