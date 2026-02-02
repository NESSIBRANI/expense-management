package com.expense.expense_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense_reports")
public class ExpenseReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Exemple: NR-2026-0001 (tu peux le générer plus tard)
    @Column(nullable = false, unique = true)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseStatus status = ExpenseStatus.DRAFT;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Employé propriétaire de la note de frais
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User employee;

    // Commentaire du manager (optionnel)
    @Column(name = "manager_comment", length = 500)
    private String managerComment;

    private LocalDateTime paidAt;

    // Les lignes (items) — on va les ajouter après (ExpenseItem)
   @OneToMany(
    mappedBy = "report",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
    private List<Expense> items = new ArrayList<>(); // TEMP: on garde Expense comme "item" pour l’instant

    public ExpenseReport() {}

    public ExpenseReport(String reference, User employee) {
        this.reference = reference;
        this.employee = employee;
        this.status = ExpenseStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
    }

    // Getters / Setters

    public Long getId() { return id; }

    public String getReference() { return reference; }

    public ExpenseStatus getStatus() { return status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public User getEmployee() { return employee; }

    public String getManagerComment() { return managerComment; }

    public List<Expense> getItems() { return items; }
    public LocalDateTime getPaidAt() {
    return paidAt;
}
public void setPaidAt(LocalDateTime paidAt) {
    this.paidAt = paidAt;
}

    public void setId(Long id) { this.id = id; }

    public void setReference(String reference) { this.reference = reference; }

    public void setStatus(ExpenseStatus status) { this.status = status; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setEmployee(User employee) { this.employee = employee; }

    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }

    public void setItems(List<Expense> items) { this.items = items; }



    // =====================
// Méthodes métier
// =====================

public void addExpense(Expense expense) {
    items.add(expense);
    expense.setReport(this);
}

public void removeExpense(Expense expense) {
    items.remove(expense);
    expense.setReport(null);
}

}
