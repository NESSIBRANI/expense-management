package com.expense.expense_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExpenseReportResponse(
        Long id,
        String reference,
        LocalDateTime createdAt,
        Long employeeId,
        List<ExpenseResponse> items,
        LocalDateTime paidAt
) {}
