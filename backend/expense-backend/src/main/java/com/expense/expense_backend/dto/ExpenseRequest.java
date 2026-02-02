package com.expense.expense_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class ExpenseRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double amount;

    private LocalDate date; // ✅ IMPORTANT

    public ExpenseRequest() {}

    public String getTitle() {
        return title;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {     // ✅ OBLIGATOIRE
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {   // ✅ OBLIGATOIRE
        this.date = date;
    }
}
