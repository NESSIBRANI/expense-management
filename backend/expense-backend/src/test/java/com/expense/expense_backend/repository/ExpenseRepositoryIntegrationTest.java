package com.expense.expense_backend.repository;

import com.expense.expense_backend.entity.Expense;
import com.expense.expense_backend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

class ExpenseRepositoryIntegrationTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User("Mohamed", "mohamed@gmail.com", "secret");
        userId = userRepository.save(user).getId();

        expenseRepository.save(new Expense("Restaurant", 150.0, LocalDate.of(2026, 1, 13), user));
        expenseRepository.save(new Expense("Restaurant", 145.0, LocalDate.of(2026, 1, 14), user));
        expenseRepository.save(new Expense("Taxi", 300.0, LocalDate.of(2026, 1, 20), user));
    }

    @Test
    void findByUserWithFilters_startDate_filtersCorrectly() {
        Page<Expense> page = expenseRepository.findByUserWithFilters(
                userId,
                null,
                null,
                LocalDate.of(2026, 1, 14),
                null,
                PageRequest.of(0, 10)
        );

        assertEquals(2, page.getContent().size());
    }

    @Test
    void findByUserWithFilters_endDate_filtersCorrectly() {
        Page<Expense> page = expenseRepository.findByUserWithFilters(
                userId,
                null,
                null,
                null,
                LocalDate.of(2026, 1, 14),
                PageRequest.of(0, 10)
        );

        assertEquals(2, page.getContent().size());
    }

    @Test
    void findByUserWithFilters_amountAndDateRange_filtersCorrectly() {
        Page<Expense> page = expenseRepository.findByUserWithFilters(
                userId,
                140.0,
                200.0,
                LocalDate.of(2026, 1, 14),
                LocalDate.of(2026, 1, 20),
                PageRequest.of(0, 10)
        );

        assertEquals(1, page.getContent().size());
    }
}
