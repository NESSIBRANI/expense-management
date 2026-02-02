package com.expense.expense_backend.controller;

import com.expense.expense_backend.dto.ExpenseMapper;
import com.expense.expense_backend.service.ExpenseReportService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.expense.expense_backend.dto.ExpenseReportResponse;


@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ExpenseReportController {

    private final ExpenseReportService reportService;

    public ExpenseReportController(ExpenseReportService reportService) {
        this.reportService = reportService;
    }

    // ============================
    // 👤 EMPLOYÉ
    // ============================

    // Créer une note de frais
   @PostMapping
    public ExpenseReportResponse create(@RequestParam Long userId) {
    return ExpenseMapper.toReportResponse(
            reportService.createReport(userId)
    );
 }

  


    // Soumettre la note
    @PutMapping("/{id}/submit")
public ExpenseReportResponse submit(@PathVariable Long id) {
    return ExpenseMapper.toReportResponse(
            reportService.submitReport(id)
    );
}

    // Mes notes de frais
    @GetMapping("/my")
public Page<ExpenseReportResponse> myReports(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
) {
    return reportService
            .myReports(userId, page, size)
            .map(ExpenseMapper::toReportResponse);
}


    // ============================
    // 👔 MANAGER
    // ============================

    // Approuver une note
   @PutMapping("/{id}/approve")
public ExpenseReportResponse approve(
        @PathVariable Long id,
        @RequestParam String comment
) {
    return ExpenseMapper.toReportResponse(
            reportService.approve(id, comment)
    );
}


    // Rejeter une note
   @PutMapping("/{id}/reject")
public ExpenseReportResponse reject(
        @PathVariable Long id,
        @RequestParam String comment
) {
    return ExpenseMapper.toReportResponse(
            reportService.reject(id, comment)
    );
}

    // Notes en attente
   @GetMapping("/pending")
public Page<ExpenseReportResponse> pending(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return reportService
            .pending(page, size)
            .map(ExpenseMapper::toReportResponse);
}

}
