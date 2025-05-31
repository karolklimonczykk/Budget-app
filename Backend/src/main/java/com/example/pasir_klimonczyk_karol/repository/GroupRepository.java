package com.example.pasir_klimonczyk_karol.repository;

import com.example.pasir_klimonczyk_karol.model.Group;
import com.example.pasir_klimonczyk_karol.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMemberships_User(User user);
}
