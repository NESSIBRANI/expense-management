package com.expense.expense_backend.controller;

import com.expense.expense_backend.dto.ExpenseRequest;
import com.expense.expense_backend.dto.ExpenseResponse;
import com.expense.expense_backend.service.ExpenseService;
import com.expense.expense_backend.dto.ExpenseListDTO;


import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin("*")
public class ExpenseController {

    private final ExpenseService expenseService;

    // ✅ Injection PROPRE par constructeur
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // ===============================
    // ➕ CREATE EXPENSE
    // ===============================
// Ajouter une dépense à un report
    @PostMapping("/report/{reportId}")
    public ExpenseResponse addExpense(
            @PathVariable Long reportId,
            @Valid @RequestBody ExpenseRequest request
    ) {
        return expenseService.createExpense(reportId, request);
    }

    @PutMapping("/{expenseId}")
public ResponseEntity<ExpenseResponse> updateExpense(
        @PathVariable Long expenseId,
        @RequestBody ExpenseRequest request
) {
    return ResponseEntity.ok(
            expenseService.updateExpense(expenseId, request)
    );
}

@DeleteMapping("/{expenseId}")
public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
    expenseService.deleteExpense(expenseId);
    return ResponseEntity.noContent().build();
}


    // ===============================
    // 📄 GET MY EXPENSES (filters + pagination)
    // ===============================
    @GetMapping("/my")
    public Page<ExpenseListDTO> getMyExpenses(
            @RequestParam Long userId,
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

        return expenseService.getMyExpenses(
                userId,
                minAmount,
                maxAmount,
                startDate,
                endDate,
                PageRequest.of(page, size)
        );
    }
}
