package com.expense.expense_backend.controller;

import com.expense.expense_backend.dto.MonthlyTotal;
import com.expense.expense_backend.service.AdminStatsService;
import org.springframework.web.bind.annotation.*;
import com.expense.expense_backend.dto.EmployeeTotal;
import java.util.List;
@RestController
@RequestMapping("/api/admin/stats")
@CrossOrigin("*")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    @GetMapping("/monthly")
    public List<MonthlyTotal> monthlyStats() {
        return adminStatsService.getMonthlyPaidTotals();
    }

    @GetMapping("/employees")
public List<EmployeeTotal> byEmployee() {
    return adminStatsService.getPaidByEmployee();
}

}


