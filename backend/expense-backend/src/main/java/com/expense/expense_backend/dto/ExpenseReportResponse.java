package com.expense.expense_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExpenseReportResponse(
        Long id,
        String reference,
        LocalDateTime createdAt,
        String status,
        Long employeeId,    
        String employeeName,
        List<ExpenseResponse> items,
        LocalDateTime paidAt
) {}
