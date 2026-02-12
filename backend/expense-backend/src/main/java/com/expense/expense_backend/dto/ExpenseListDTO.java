package com.expense.expense_backend.dto;

import com.expense.expense_backend.entity.ExpenseStatus;
import java.time.LocalDate;

public class ExpenseListDTO {

    private Long id;
    private String title;
    private Double amount;
    private LocalDate date;
    private ExpenseStatus status;   // ✅ AJOUTÉ
    private UserDTO user;

    public ExpenseListDTO() {}

    // ✅ CONSTRUCTEUR COMPLET
    public ExpenseListDTO(
            Long id,
            String title,
            Double amount,
            LocalDate date,
            ExpenseStatus status,
            UserDTO user
    ) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.user = user;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public ExpenseStatus getStatus() { return status; }
    public UserDTO getUser() { return user; }
}
