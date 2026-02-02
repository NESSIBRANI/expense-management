package com.expense.expense_backend.repository;

import com.expense.expense_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Already exists (KEEP)
    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);


    Optional<User> findByEmailAndEnabledTrue(String email);


    long countByRole(String role);
}
