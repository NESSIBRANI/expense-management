package com.expense.expense_backend.controller;

import com.expense.expense_backend.service.AdminExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/export")
@CrossOrigin("*")
public class AdminExportController {

    private final AdminExportService exportService;

    public AdminExportController(AdminExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/excel/employees")
    public ResponseEntity<byte[]> exportExcel() {
        byte[] file = exportService.exportExpensesByEmployeeExcel();

        String filename = "depenses_par_employe_" + LocalDate.now() + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
