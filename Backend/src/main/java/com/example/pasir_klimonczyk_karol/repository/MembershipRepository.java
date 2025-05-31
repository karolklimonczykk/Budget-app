package com.example.pasir_klimonczyk_karol.repository;

import com.example.pasir_klimonczyk_karol.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByGroupId(Long groupId);
}
