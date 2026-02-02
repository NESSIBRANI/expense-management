package com.expense.expense_backend.repository;

import com.expense.expense_backend.entity.ExpenseReport;
import com.expense.expense_backend.entity.ExpenseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {
    Optional<ExpenseReport> findByReference(String reference);
   
    @EntityGraph(attributePaths = {"items", "employee"})
    Page<ExpenseReport> findByStatus(ExpenseStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "employee"})
    Page<ExpenseReport> findByEmployeeId(Long employeeId, Pageable pageable);

}
