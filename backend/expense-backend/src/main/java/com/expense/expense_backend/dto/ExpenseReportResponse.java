package com.expense.expense_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExpenseReportResponse(
        Long id,
        String reference,
        LocalDateTime createdAt,
        Long employeeId,
        String status,
        String employeeName,
        List<ExpenseResponse> items,
        LocalDateTime paidAt
) {}
