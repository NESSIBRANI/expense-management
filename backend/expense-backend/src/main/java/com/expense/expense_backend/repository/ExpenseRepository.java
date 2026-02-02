package com.expense.expense_backend.repository;

import com.expense.expense_backend.dto.EmployeeTotal;
import com.expense.expense_backend.dto.MonthlyTotal;
import com.expense.expense_backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
@Query("""
    SELECT e FROM Expense e
WHERE e.user.id = :userId
AND e.amount >= COALESCE(:minAmount, e.amount)
AND e.amount <= COALESCE(:maxAmount, e.amount)
AND e.date >= COALESCE(:startDate, e.date)
AND e.date <= COALESCE(:endDate, e.date)

""")
Page<Expense> findByUserWithFilters(
        @Param("userId") Long userId,
        @Param("minAmount") Double minAmount,
        @Param("maxAmount") Double maxAmount,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
);



    // 🔹 Stats par employé
    
    @Query("""
        SELECT 
            e.user.id AS employeeId,
            e.user.name AS employeeName,
            SUM(e.amount) AS total
        FROM Expense e
        WHERE e.status = 'PAID'
        GROUP BY e.user.id, e.user.name
    """)
    List<EmployeeTotal> totalPaidByEmployee();

    @Query("""
        SELECT 
            FUNCTION('TO_CHAR', e.date, 'YYYY-MM') AS month,
            SUM(e.amount) AS total
        FROM Expense e
        WHERE e.status = 'PAID'
        GROUP BY FUNCTION('TO_CHAR', e.date, 'YYYY-MM')
        ORDER BY month
    """)
    List<MonthlyTotal> totalPaidByMonth();
}
