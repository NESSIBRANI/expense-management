package com.expense.expense_backend.service;

import com.expense.expense_backend.repository.ExpenseRepository;
import com.expense.expense_backend.dto.EmployeeTotal;
import com.expense.expense_backend.dto.MonthlyTotal;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminStatsService {

    private final ExpenseRepository expenseRepository;

    public AdminStatsService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<MonthlyTotal> getMonthlyPaidTotals() {
        return expenseRepository.totalPaidByMonth();
    }

   public List<EmployeeTotal> getPaidByEmployee() {
    return expenseRepository.totalPaidByEmployee();
}
}
