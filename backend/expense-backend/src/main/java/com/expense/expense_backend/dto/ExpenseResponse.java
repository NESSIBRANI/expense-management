package com.expense.expense_backend.dto;

import com.expense.expense_backend.entity.ExpenseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponse {

    private Long id;
    private String title;
    private Double amount;
    private LocalDate date;
    private ExpenseStatus status;
    private LocalDateTime createdAt;
    private String managerComment;
    private Long userId;

    public ExpenseResponse(
            Long id,
            String title,
            Double amount,
            LocalDate date,
            ExpenseStatus status,
            LocalDateTime createdAt,
            String managerComment,
            Long userId
    ) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.createdAt = createdAt;
        this.managerComment = managerComment;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public ExpenseStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getManagerComment() { return managerComment; }
    public Long getUserId() { return userId; }
}
