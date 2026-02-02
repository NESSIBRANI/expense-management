package com.expense.expense_backend.dto;

import java.time.LocalDate;

public class ExpenseListDTO {

    private Long id;
    private String title;
    private Double amount;
    private LocalDate date;
    private UserDTO user;

    public ExpenseListDTO() {}

    public ExpenseListDTO(Long id, String title, Double amount, LocalDate date, UserDTO user) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.user = user;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
