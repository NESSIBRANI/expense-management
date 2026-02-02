package com.expense.expense_backend.service;

import com.expense.expense_backend.dto.ExpenseListDTO;
import com.expense.expense_backend.dto.ExpenseMapper;
import com.expense.expense_backend.dto.ExpenseRequest;
import com.expense.expense_backend.dto.ExpenseResponse;
import com.expense.expense_backend.dto.UserDTO;
import com.expense.expense_backend.entity.Expense;
import com.expense.expense_backend.entity.ExpenseReport;
import com.expense.expense_backend.entity.ExpenseStatus;
import com.expense.expense_backend.entity.User;
import com.expense.expense_backend.exception.BusinessException;
import com.expense.expense_backend.exception.ResourceNotFoundException;
import com.expense.expense_backend.repository.ExpenseReportRepository;
import com.expense.expense_backend.repository.ExpenseRepository;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseReportRepository expenseReportRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            ExpenseReportRepository expenseReportRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.expenseReportRepository = expenseReportRepository;
    }
@Transactional
    public ExpenseResponse createExpense(Long reportId, ExpenseRequest request) {

    ExpenseReport report = expenseReportRepository.findById(reportId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Report not found with id " + reportId)
            );

    if (report.getStatus() != ExpenseStatus.DRAFT) {
        throw new BusinessException(
                "Cannot add expense when report is not DRAFT"
        );
    }

    User user = report.getEmployee();

    Expense expense = new Expense();
    expense.setTitle(request.getTitle());
    expense.setAmount(request.getAmount());
    expense.setDate(
            request.getDate() != null ? request.getDate() : LocalDate.now()
    );
    expense.setStatus(ExpenseStatus.DRAFT);
    expense.setCreatedAt(LocalDateTime.now());
    expense.setReport(report);
    expense.setUser(user);

    Expense saved = expenseRepository.save(expense);
     saved.getUser().getName();
    return ExpenseMapper.toExpenseResponse(saved);
}


     // ============================
    // ✏️ MODIFIER UNE DEPENSE
    // ============================
    @Transactional
   public ExpenseResponse updateExpense(Long expenseId, ExpenseRequest request) {

    Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Expense not found with id " + expenseId)
            );

    ExpenseReport report = expense.getReport();

    if (report.getStatus() != ExpenseStatus.DRAFT) {
        throw new BusinessException(
                "Cannot modify expense when report status is " + report.getStatus()
        );
    }

    expense.setTitle(request.getTitle());
    expense.setAmount(request.getAmount());
    expense.setDate(request.getDate());

    expenseRepository.save(expense);

    return ExpenseMapper.toExpenseResponse(expense);
}


    // ============================
    // 🗑️ SUPPRIMER UNE DEPENSE
    // ============================
@Transactional
   public void deleteExpense(Long expenseId) {

    Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Expense not found with id " + expenseId)
            );

    if (expense.getReport().getStatus() != ExpenseStatus.DRAFT) {
        throw new BusinessException(
                "Cannot delete expense when report is not DRAFT"
        );
    }

    expenseRepository.delete(expense);
}


    // ===============================
    // 📄 GET MY EXPENSES (filters)
    // ===============================
    public Page<ExpenseListDTO> getMyExpenses(
            Long userId,
            Double minAmount,
            Double maxAmount,
            LocalDate startDate,
            LocalDate endDate,
            PageRequest pageable
    ) {

        return expenseRepository
                .findByUserWithFilters(
                        userId,
                        minAmount,
                        maxAmount,
                        startDate,
                        endDate,
                        pageable
                )
                .map(expense -> new ExpenseListDTO(
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
