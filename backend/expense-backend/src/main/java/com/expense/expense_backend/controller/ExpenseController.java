package com.expense.expense_backend.controller;

import com.expense.expense_backend.dto.ExpenseRequest;
import com.expense.expense_backend.dto.ExpenseResponse;
import com.expense.expense_backend.entity.Expense;
import com.expense.expense_backend.entity.User;
import com.expense.expense_backend.repository.ExpenseRepository;
import com.expense.expense_backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;


import com.expense.expense_backend.dto.ExpenseDTO;
import com.expense.expense_backend.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/expenses")
@CrossOrigin("*")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseRepository expenseRepository,
                             UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }
@PostMapping
public ExpenseResponse createExpense( @Valid @RequestBody ExpenseRequest request) {

    User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Expense expense = new Expense();
    expense.setTitle(request.getTitle());
    expense.setAmount(request.getAmount());
    expense.setDate(LocalDate.now());
    expense.setUser(user);

    Expense saved = expenseRepository.save(expense);

    return new ExpenseResponse(
            saved.getId(),
            saved.getTitle(),
            saved.getAmount(),
            saved.getDate(),
            user.getId(),
            user.getName(),
            user.getEmail()
    );
}



@GetMapping("/my")
public Page<ExpenseDTO> getMyExpenses(
        @RequestParam Long userId,   // ✅ TEMPORAIRE
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,

        @RequestParam(required = false) Double minAmount,
        @RequestParam(required = false) Double maxAmount,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {

    PageRequest pageable = PageRequest.of(page, size);

    return expenseRepository
            .findByUserWithFilters(
                    userId,
                    minAmount,
                    maxAmount,
                    startDate,
                    endDate,
                    pageable
            )
            .map(expense -> new ExpenseDTO(
                    expense.getId(),
                    expense.getTitle(),
                    expense.getAmount(),
                    expense.getDate(),
                    new UserDTO(
                            expense.getUser().getId(),
                            expense.getUser().getName(),
                            expense.getUser().getEmail()
                    )
            ));
}


}
