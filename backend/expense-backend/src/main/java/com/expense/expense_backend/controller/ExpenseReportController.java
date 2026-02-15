package com.expense.expense_backend.controller;

import com.expense.expense_backend.dto.ExpenseMapper;
import com.expense.expense_backend.dto.ExpenseReportResponse;
import com.expense.expense_backend.dto.ExpenseRequest;
import com.expense.expense_backend.dto.ExpenseResponse;

import com.expense.expense_backend.service.ExpenseReportService;



import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.expense.expense_backend.service.ExpenseService;


import java.util.Map;





@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class ExpenseReportController {

    private final ExpenseReportService reportService;
    private final ExpenseService expenseService;


    public ExpenseReportController(
        ExpenseReportService reportService,
        ExpenseService expenseService
) {
    this.reportService = reportService;
    this.expenseService = expenseService;
}

    // ============================
    // 👤 EMPLOYÉ
    // ============================

    // 🔹 Créer une note de frais (UTILISE LE TOKEN)
    @PostMapping
    public ExpenseReportResponse create(Authentication authentication) {

        String email = authentication.getName();

        return ExpenseMapper.toReportResponse(
                reportService.createReport(email)
        );
    }

    // 🔹 Soumettre la note
   @PutMapping("/{id}/submit")
public ResponseEntity<ExpenseReportResponse> submit(@PathVariable Long id) {
    return ResponseEntity.ok(reportService.submitReport(id));
}


 // 🔍 DETAIL D’UNE NOTE DE FRAIS POUR L’EMPLOYÉ CONNECTÉ
@GetMapping("/my/{id}")
public ExpenseReportResponse getMyReport(
        @PathVariable Long id,
        Authentication authentication) {

    return ExpenseMapper.toReportResponse(
            reportService.getMyReport(id, authentication.getName())
    );
}

 /// ➕ Ajouter une dépense à une note de frais
@PostMapping("/{reportId}/expenses")
public ExpenseResponse addExpenseToReport(
        @PathVariable Long reportId,
        @RequestBody ExpenseRequest request
) {
    return expenseService.createExpense(reportId, request);
}


    // 🔹 Mes notes de frais (UTILISE LE TOKEN)
    @GetMapping("/my")
    public Page<ExpenseReportResponse> myReports(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        String email = authentication.getName();

        return reportService
                .myReports(email, page, size)
                .map(ExpenseMapper::toReportResponse);
    }


    @DeleteMapping("/{id}")
public void deleteReport(@PathVariable Long id, Authentication authentication) {
    String email = authentication.getName();
    reportService.deleteReport(id, email);
}

// ============================
// 👔 MANAGER ACTIONS
// ============================
@PutMapping("/{id}/approve")
public ResponseEntity<ExpenseReportResponse> approve(@PathVariable Long id) {
    return ResponseEntity.ok(
        ExpenseMapper.toReportResponse(reportService.approve(id, null))
    );
}

@PutMapping("/{id}/reject")
public ResponseEntity<ExpenseReportResponse> reject(@PathVariable Long id) {
    return ResponseEntity.ok(
        ExpenseMapper.toReportResponse(reportService.reject(id, null))
    );
}




@GetMapping("/stats")
public Map<String, Long> stats() {
    return reportService.getStats();
}



    // Notes en attente
   @GetMapping("/pending")
public Page<ExpenseReportResponse> pending(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return reportService.pending(page, size)
            .map(ExpenseMapper::toReportResponse);
}

    // 👔 MANAGER - GET REPORT DETAILS
@GetMapping("/{id}")
public ResponseEntity<ExpenseReportResponse> getReportDetails(
        @PathVariable Long id
) {
    return ResponseEntity.ok(reportService.getReportForManager(id));
}


}
