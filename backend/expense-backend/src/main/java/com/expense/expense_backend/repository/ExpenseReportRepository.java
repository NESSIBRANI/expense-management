package com.expense.expense_backend.repository;

import com.expense.expense_backend.entity.ExpenseReport;
import com.expense.expense_backend.entity.ExpenseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {

    Optional<ExpenseReport> findByReference(String reference);
    long countByStatus(ExpenseStatus status);


    // ⭐ IMPORTANT (chargement des dépenses du report)
    @EntityGraph(attributePaths = {"items", "employee"})
    Optional<ExpenseReport> findWithItemsById(Long id);

   @EntityGraph(attributePaths = {"items", "employee"})
Page<ExpenseReport> findByStatus(ExpenseStatus status, Pageable pageable);


    @EntityGraph(attributePaths = {"items", "employee"})
    Page<ExpenseReport> findByEmployeeId(Long employeeId, Pageable pageable);




@Query("""
SELECT r FROM ExpenseReport r
LEFT JOIN FETCH r.employee
LEFT JOIN FETCH r.items
WHERE r.id = :id
""")
Optional<ExpenseReport> findFullReport(@Param("id") Long id);

}
