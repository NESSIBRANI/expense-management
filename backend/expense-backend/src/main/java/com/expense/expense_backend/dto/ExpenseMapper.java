package com.expense.expense_backend.dto;

import com.expense.expense_backend.entity.Expense;
import com.expense.expense_backend.entity.ExpenseReport;

import java.util.List;

public class ExpenseMapper {

    // ==========================
    // EXPENSE → DTO
    // ==========================
    public static ExpenseResponse toExpenseResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(),
                e.getTitle(),
                e.getAmount(),
                e.getDate(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getManagerComment(),
                e.getUser().getId()
        );
    }

    // ==========================
    // REPORT → DTO
    // ==========================
    public static ExpenseReportResponse toReportResponse(ExpenseReport r) {

        List<ExpenseResponse> items =
                r.getItems() == null
                        ? List.of()
                        : r.getItems().stream()
                                .map(ExpenseMapper::toExpenseResponse)
                                .toList();

        return new ExpenseReportResponse(
                r.getId(),
                r.getReference(),
                r.getCreatedAt(),
                r.getEmployee().getId(),
                items,
                r.getPaidAt()
        );
    }
}
