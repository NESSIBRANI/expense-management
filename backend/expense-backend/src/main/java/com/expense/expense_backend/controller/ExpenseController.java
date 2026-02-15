package com.expense.expense_backend.controller;

import com.expense.expense_backend.dto.ExpenseRequest;
import com.expense.expense_backend.dto.ExpenseResponse;
import com.expense.expense_backend.service.ExpenseService;
import com.expense.expense_backend.dto.ExpenseListDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;


import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;

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
//

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

@GetMapping("/report/{reportId}")
public List<ExpenseListDTO> getExpensesByReport(
        @PathVariable Long reportId,
        Authentication authentication
) {
    String email = authentication.getName();
    return expenseService.getExpensesByReport(reportId, email);
}


    // ===============================
    // 📄 GET MY EXPENSES (filters + pagination)
    // ===============================
  @GetMapping("/my")
public Page<ExpenseListDTO> getMyExpenses(
        Authentication authentication,
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

    // 🔐 récupérer l'utilisateur connecté
    String email = authentication.getName();

    PageRequest pageable =
            PageRequest.of(page, size, Sort.by("createdAt").descending());

    // ⚠️ ICI ON PASSE L'EMAIL AU SERVICE
    return expenseService.getMyExpenses(
            email,
            minAmount,
            maxAmount,
            startDate,
            endDate,
            pageable
    );
}


}
