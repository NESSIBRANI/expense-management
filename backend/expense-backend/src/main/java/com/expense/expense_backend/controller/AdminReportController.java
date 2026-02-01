package com.expense.expense_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.expense_backend.dto.ExpenseMapper;
import com.expense.expense_backend.service.ExpenseReportService;
import com.expense.expense_backend.dto.ExpenseReportResponse;


    @RestController
@RequestMapping("/api/admin/reports")
@CrossOrigin("*")
public class AdminReportController {

    private final ExpenseReportService reportService;

    public AdminReportController(ExpenseReportService reportService) {
        this.reportService = reportService;
    }

    // 💰 Remboursement
    @PutMapping("/{id}/pay")
    public ExpenseReportResponse pay(@PathVariable Long id) {
        return ExpenseMapper.toReportResponse(
                reportService.pay(id)
        );
    }
}

    

