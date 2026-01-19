package com.expense.expense_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Double amount;

   
private LocalDate date;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 🔹 Constructeurs
    public Expense() {}

    public Expense(String title, Double amount, LocalDate date, User user) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.user = user;
    }

    // 🔹 Getters & Setters
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

    public User getUser() {
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

    public void setUser(User user) {
        this.user = user;
    }
}
