package com.expense.expense_backend.service;

import com.expense.expense_backend.dto.ExpenseMapper;
import com.expense.expense_backend.dto.ExpenseReportResponse;
import com.expense.expense_backend.entity.*;
import com.expense.expense_backend.exception.BusinessException;
import com.expense.expense_backend.exception.ResourceNotFoundException;
import com.expense.expense_backend.repository.ExpenseReportRepository;
import com.expense.expense_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpenseReportService {

    private final ExpenseReportRepository reportRepository;
    private final UserRepository userRepository;

    // ======================================================
    // 🔐 UTILITAIRE : récupérer utilisateur depuis JWT
    // ======================================================
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );
    }

    // ======================================================
    // 🧾 CREATE REPORT
    // ======================================================
    @Transactional
    public ExpenseReport createReport(String email) {

        User user = getUserByEmail(email);

        ExpenseReport report = new ExpenseReport();
        report.setEmployee(user);
        report.setStatus(ExpenseStatus.DRAFT);
        report.setCreatedAt(LocalDateTime.now());

        // référence automatique
        report.setReference("NR-" + System.currentTimeMillis());

        reportRepository.save(report);
        reportRepository.flush(); // IMPORTANT pour Angular

        return report;
    }

    // ======================================================
    // 📤 SUBMIT REPORT
    // ======================================================
    @Transactional
    public ExpenseReportResponse submitReport(Long id) {

        ExpenseReport report = reportRepository.findWithItemsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found")
                );

        if (report.getItems().isEmpty()) {
            throw new BusinessException("Cannot submit an empty report");
        }

        report.setStatus(ExpenseStatus.SUBMITTED);

        // Force l’écriture immédiate en base
        reportRepository.flush();

        return ExpenseMapper.toReportResponse(report);
    }

    // ======================================================
    // 📄 GET MY REPORT
    // ======================================================
    @Transactional(readOnly = true)
    public ExpenseReport getMyReport(Long reportId, String email) {

        User user = getUserByEmail(email);

        ExpenseReport report = reportRepository.findWithItemsById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report introuvable")
                );

        if (!report.getEmployee().getId().equals(user.getId())) {
            throw new BusinessException("Access denied to this report");
        }

        // force lazy loading
        report.getItems().size();

        return report;
    }

    // ======================================================
    // 🗑 DELETE REPORT
    // ======================================================
    @Transactional
    public void deleteReport(Long reportId, String email) {

        User user = getUserByEmail(email);

        ExpenseReport report = reportRepository.findWithItemsById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found")
                );

        if (!report.getEmployee().getId().equals(user.getId())) {
            throw new BusinessException("You cannot delete this report");
        }

        if (report.getStatus() != ExpenseStatus.DRAFT) {
            throw new BusinessException("Only DRAFT reports can be deleted");
        }

        reportRepository.delete(report);
    }

    // ======================================================
    // 👨‍💼 MANAGER ACTIONS
    // ======================================================

    @Transactional
    public ExpenseReport approve(Long reportId, String comment) {

        ExpenseReport report = reportRepository.findFullReport(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found")
                );

        if (report.getStatus() != ExpenseStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED reports can be approved");
        }

        report.setStatus(ExpenseStatus.APPROVED);
        report.setManagerComment(
                (comment != null && !comment.isBlank())
                        ? comment
                        : "Approved by manager"
        );

        // mettre toutes les dépenses en approved
        report.getItems().forEach(expense ->
                expense.setStatus(ExpenseStatus.APPROVED)
        );

        return reportRepository.save(report);
    }

    @Transactional
    public ExpenseReport reject(Long reportId, String comment) {

        ExpenseReport report = reportRepository.findFullReport(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found")
                );

        if (report.getStatus() != ExpenseStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED reports can be rejected");
        }

        report.setStatus(ExpenseStatus.REJECTED);
        report.setManagerComment(
                (comment != null && !comment.isBlank())
                        ? comment
                        : "Rejected by manager"
        );

        report.getItems().forEach(expense ->
                expense.setStatus(ExpenseStatus.REJECTED)
        );

        return reportRepository.save(report);
    }

    // ======================================================
    // 📊 EMPLOYEE REPORTS
    // ======================================================
    @Transactional(readOnly = true)
    public Page<ExpenseReport> myReports(String email, int page, int size) {

        User user = getUserByEmail(email);

        return reportRepository.findByEmployeeId(
                user.getId(),
                PageRequest.of(page, size)
        );
    }




    // ======================================================
// 👨‍💼 GET REPORT DETAILS FOR MANAGER
// ======================================================
@Transactional(readOnly = true)
public ExpenseReportResponse getReportForManager(Long id) {

    ExpenseReport report = reportRepository.findFullReport(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Report not found")
            );

    return ExpenseMapper.toReportResponse(report);
}

    // ======================================================
    // ⏳ PENDING REPORTS (Manager)
    // ======================================================
    @Transactional(readOnly = true)
    public Page<ExpenseReport> pending(int page, int size) {
        return reportRepository.findByStatus(
                ExpenseStatus.SUBMITTED,
                PageRequest.of(page, size)
        );
    }

    // ======================================================
    // 📊 STATS (Admin Dashboard)
    // ======================================================
    @Transactional(readOnly = true)
    public Map<String, Long> getStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("pending", reportRepository.countByStatus(ExpenseStatus.SUBMITTED));
        stats.put("approved", reportRepository.countByStatus(ExpenseStatus.APPROVED));
        stats.put("rejected", reportRepository.countByStatus(ExpenseStatus.REJECTED));
        stats.put("paid", reportRepository.countByStatus(ExpenseStatus.PAID));

        return stats;
    }

    // ======================================================
    // 💰 PAY REPORT (Admin)
    // ======================================================
    @Transactional
    public ExpenseReport pay(Long reportId) {

        ExpenseReport report = reportRepository.findFullReport(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found")
                );

        if (report.getStatus() != ExpenseStatus.APPROVED) {
            throw new BusinessException("Only APPROVED reports can be paid");
        }

        report.setStatus(ExpenseStatus.PAID);
        report.setPaidAt(LocalDateTime.now());

        report.getItems().forEach(expense ->
                expense.setStatus(ExpenseStatus.PAID)
        );

        return reportRepository.save(report);
    }

    // ======================================================
    // 🔎 FIND BY STATUS
    // ======================================================
    @Transactional(readOnly = true)
    public Page<ExpenseReport> findByStatus(ExpenseStatus status, int page, int size) {
        return reportRepository.findByStatus(status, PageRequest.of(page, size));
    }
}
