package com.expense.expense_backend.dto;

import com.expense.expense_backend.entity.*;

import java.util.List;

public class ExpenseMapper {

    public static ExpenseResponse toExpenseResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(),
                e.getTitle(),
                e.getAmount(),
                e.getDate(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getManagerComment(),
                e.getUser().getId(),
                e.getUser().getName(),
                e.getUser().getEmail()
        );
    }

   public static ExpenseReportResponse toReportResponse(ExpenseReport r) {

    List<ExpenseResponse> items =
            r.getItems() == null ? List.of()
                    : r.getItems().stream()
                        .map(ExpenseMapper::toExpenseResponse)
                        .toList();

    return new ExpenseReportResponse(
            r.getId(),
            r.getReference(),
            r.getCreatedAt(),
            r.getEmployee().getId(),
            r.getEmployee().getName(),
            r.getEmployee().getEmail(),
            items,
            r.getPaidAt() // ✅ ICI (sur report, pas sur items)
    );
}
}
