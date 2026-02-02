package com.expense.expense_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Double amount;

    private LocalDate date;

    // 🔹 Statut du workflow (DRAFT, SUBMITTED, APPROVED, REJECTED, PAID)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseStatus status;

    // 🔹 Date de création
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 🔹 Commentaire du manager
    @Column(length = 500)
    private String managerComment;

    // 🔹 L’utilisateur qui a créé la dépense
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔹 La note de frais (ExpenseReport) à laquelle appartient la dépense
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "report_id")
private ExpenseReport report;


    // =========================
    // 🔹 CONSTRUCTEURS
    // =========================

    // Constructeur requis par JPA
    public Expense() {
        this.createdAt = LocalDateTime.now();
        this.status = ExpenseStatus.DRAFT;
    }

    // 🔹 Constructeur utilisé par les tests et création rapide
    public Expense(String title, Double amount, LocalDate date, User user) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.status = ExpenseStatus.DRAFT;
    }

    // =========================
    // 🔹 GETTERS
    // =========================

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

    public ExpenseStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public User getUser() {
        return user;
    }
public ExpenseReport getReport() {
    return report;
}


    // =========================
    // 🔹 SETTERS
    // =========================

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

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReport(ExpenseReport report) {
    this.report = report;
}

}
