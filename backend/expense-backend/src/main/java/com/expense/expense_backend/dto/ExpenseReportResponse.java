package com.expense.expense_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ExpenseReportResponse {

    private Long id;
    private String reference;
    private LocalDateTime createdAt;

    private Long employeeId;
    private String employeeName;
    private String employeeEmail;

    private List<ExpenseResponse> items;

    private LocalDateTime paidAt; // ✅ champ réel

    public ExpenseReportResponse(
            Long id,
            String reference,
            LocalDateTime createdAt,
            Long employeeId,
            String employeeName,
            String employeeEmail,
            List<ExpenseResponse> items,
            LocalDateTime paidAt
    ) {
        this.id = id;
        this.reference = reference;
        this.createdAt = createdAt;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.items = items;
        this.paidAt = paidAt;
    }

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getReference() { return reference; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getEmployeeEmail() { return employeeEmail; }
    public List<ExpenseResponse> getItems() { return items; }
    public LocalDateTime getPaidAt() { return paidAt; }
}
