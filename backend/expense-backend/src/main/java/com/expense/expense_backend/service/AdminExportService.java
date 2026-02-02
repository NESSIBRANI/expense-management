package com.expense.expense_backend.service;

import com.expense.expense_backend.dto.EmployeeTotal;
import com.expense.expense_backend.repository.ExpenseRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class AdminExportService {

    private final ExpenseRepository expenseRepository;

    public AdminExportService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public byte[] exportExpensesByEmployeeExcel() {
        List<EmployeeTotal> data = expenseRepository.totalPaidByEmployee();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Dépenses par employé");

            // ===============================
            // STYLE HEADER (GRAS)
            // ===============================
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            // ===============================
            // STYLE MONNAIE
            // ===============================
            CellStyle moneyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            moneyStyle.setDataFormat(format.getFormat("#,##0.00"));

            // ===============================
            // HEADER
            // ===============================
            Row header = sheet.createRow(0);

            String[] columns = {"Employee ID", "Nom", "Total remboursé"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===============================
            // DATA
            // ===============================
            int rowIdx = 1;
            for (EmployeeTotal e : data) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(e.getEmployeeId());
                row.createCell(1).setCellValue(e.getEmployeeName());

                Cell totalCell = row.createCell(2);
                totalCell.setCellValue(e.getTotal());
                totalCell.setCellStyle(moneyStyle);
            }

            // ===============================
            // AUTO SIZE
            // ===============================
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ===============================
            // EXPORT
            // ===============================
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur export Excel", e);
        }
    }
}
