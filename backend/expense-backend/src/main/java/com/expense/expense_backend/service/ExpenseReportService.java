package com.expense.expense_backend.service;

import com.expense.expense_backend.dto.ExpenseRequest;
import com.expense.expense_backend.entity.*;
import com.expense.expense_backend.exception.BusinessException;
import com.expense.expense_backend.exception.ResourceNotFoundException;
import com.expense.expense_backend.repository.ExpenseReportRepository;
import com.expense.expense_backend.repository.ExpenseRepository;
import com.expense.expense_backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
public class ExpenseReportService {

    private final ExpenseReportRepository reportRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseReportService(
            ExpenseReportRepository reportRepository,
            ExpenseRepository expenseRepository,
            UserRepository userRepository
    ) {
        this.reportRepository = reportRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // ======================
    // CREATE REPORT
    // ======================
    @Transactional
    public ExpenseReport createReport(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ExpenseReport report = new ExpenseReport();
        report.setEmployee(user);
        report.setStatus(ExpenseStatus.DRAFT);
        report.setCreatedAt(LocalDateTime.now());
        report.setReference("NR-" + System.currentTimeMillis());

        return reportRepository.save(report);
    }

    // ======================
    // ADD ITEM
    // ======================
    @Transactional
    public Expense addItem(Long reportId, ExpenseRequest request) {

        ExpenseReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (report.getStatus() != ExpenseStatus.DRAFT) {
            throw new BusinessException("Cannot add expense to non-DRAFT report");
        }

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setStatus(ExpenseStatus.DRAFT);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUser(report.getEmployee());
        expense.setReport(report);

        return expenseRepository.save(expense);
    }

    // ======================
    // SUBMIT REPORT
    // ======================
    @Transactional
    public ExpenseReport submitReport(Long reportId) {

        ExpenseReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (report.getStatus() != ExpenseStatus.DRAFT) {
            throw new BusinessException("Only DRAFT reports can be submitted");
        }

        if (report.getItems().isEmpty()) {
            throw new BusinessException("Cannot submit an empty report");
        }

        report.getItems().forEach(e ->
                e.setStatus(ExpenseStatus.SUBMITTED)
        );

        report.setStatus(ExpenseStatus.SUBMITTED);
        return report;
    }

    // ======================
    // MANAGER ACTIONS
    // ======================
    @Transactional
    public ExpenseReport approve(Long reportId, String comment) {

        ExpenseReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (report.getStatus() != ExpenseStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED reports can be approved");
        }

        report.setStatus(ExpenseStatus.APPROVED);
        report.setManagerComment(comment);

        report.getItems().forEach(i ->
                i.setStatus(ExpenseStatus.APPROVED)
        );

        return report;
    }

    @Transactional
    public ExpenseReport reject(Long reportId, String comment) {

        ExpenseReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (report.getStatus() != ExpenseStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED reports can be rejected");
        }

        report.setStatus(ExpenseStatus.REJECTED);
        report.setManagerComment(comment);

        report.getItems().forEach(i ->
                i.setStatus(ExpenseStatus.REJECTED)
        );

        return report;
    }

    // ======================
    // LISTINGS (IMPORTANT)
    // ======================
    @Transactional(readOnly = true)
    public Page<ExpenseReport> myReports(Long userId, int page, int size) {
        return reportRepository.findByEmployeeId(
                userId, PageRequest.of(page, size)
        );
    }

    @Transactional(readOnly = true)
    public Page<ExpenseReport> pending(int page, int size) {
        return reportRepository.findByStatus(
                ExpenseStatus.SUBMITTED,
                PageRequest.of(page, size)
        );
    }

    // ======================
    // PAY
    // ======================
    @Transactional
    public ExpenseReport pay(Long reportId) {

        ExpenseReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (report.getStatus() != ExpenseStatus.APPROVED) {
            throw new BusinessException("Only APPROVED reports can be paid");
        }

        report.setStatus(ExpenseStatus.PAID);
        report.setPaidAt(LocalDateTime.now());

        report.getItems().forEach(i ->
                i.setStatus(ExpenseStatus.PAID)
        );

        return report;
    }
}
