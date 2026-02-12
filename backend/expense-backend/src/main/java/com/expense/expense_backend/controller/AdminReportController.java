package com.expense.expense_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;

import com.expense.expense_backend.entity.ExpenseStatus;
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

    // 💰 PAY
    @PutMapping("/{id}/pay")
    public ExpenseReportResponse pay(@PathVariable Long id) {
        return ExpenseMapper.toReportResponse(
                reportService.pay(id)
        );
    }

    // ✅ FETCH APPROVED REPORTS (FOR ADMIN)
    @GetMapping("/approved")
    public Page<ExpenseReportResponse> approved(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return reportService
                .findByStatus(ExpenseStatus.APPROVED, page, size)
                .map(ExpenseMapper::toReportResponse);
    }

    // ✅ FETCH PAID REPORTS (OPTIONAL BUT CLEAN)
    @GetMapping("/paid")
    public Page<ExpenseReportResponse> paid(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return reportService
                .findByStatus(ExpenseStatus.PAID, page, size)
                .map(ExpenseMapper::toReportResponse);
    }
    // 👀 GET ALL REPORTS (ADMIN SEES EVERYTHING)
    @GetMapping
    public Page<ExpenseReportResponse> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return reportService.findAll(page, size)
                .map(ExpenseMapper::toReportResponse);
    }
}

    

