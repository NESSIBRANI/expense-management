package com.expense.expense_backend.dto;

import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;
    private String title;
    private Double amount;
    private LocalDate date;

    private Long userId;
    private String userName;
    private String userEmail;

    public ExpenseResponse() {}

    public ExpenseResponse(Long id, String title, Double amount, LocalDate date,
                           Long userId, String userName, String userEmail) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
}
