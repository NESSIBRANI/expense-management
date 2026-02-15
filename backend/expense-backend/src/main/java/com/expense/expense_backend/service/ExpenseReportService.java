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

import java.util.Map;


import java.time.LocalDateTime;
import java.util.HashMap;



@Service
@RequiredArgsConstructor
public class ExpenseReportService {

    private final ExpenseReportRepository reportRepository;
   
    private final UserRepository userRepository;


    // ======================
    // 🔐 UTILITAIRE : récupérer utilisateur depuis JWT
    // ======================
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );
    }

    // ======================
    // CREATE REPORT
    // ======================
    @Transactional
    public ExpenseReport createReport(String email) {

        User user = getUserByEmail(email);

        ExpenseReport report = new ExpenseReport();
        report.setEmployee(user);
        report.setStatus(ExpenseStatus.DRAFT);
        report.setCreatedAt(LocalDateTime.now());
        report.setReference("NR-" + System.currentTimeMillis());

        return reportRepository.save(report);
    }

    // ======================
    // SUBMIT REPORT
    // ======================
  @Transactional
public ExpenseReportResponse submitReport(Long id) {

    ExpenseReport report = reportRepository.findWithItemsById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

    // empêcher soumission vide
    if (report.getItems().isEmpty()) {
        throw new BusinessException("Cannot submit an empty report");
    }

    report.setStatus(ExpenseStatus.SUBMITTED);

    // ⭐ FORCE L'ÉCRITURE EN BASE IMMÉDIATEMENT
    reportRepository.flush();

    return ExpenseMapper.toReportResponse(report);
}


    // ======================
    // GET MY REPORT
    // ======================
@Transactional(readOnly = true)
public ExpenseReport getMyReport(Long reportId, String email) {

    User user = getUserByEmail(email);

    ExpenseReport report = reportRepository.findWithItemsById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report introuvable"));

    if (!report.getEmployee().getId().equals(user.getId())) {
        throw new BusinessException("Access denied to this report");
    }

    report.getItems().size(); // force lazy loading

    return report;
}


// ======================
// DELETE REPORT
// ======================
@Transactional
public void deleteReport(Long reportId, String email) {

    User user = getUserByEmail(email);

    ExpenseReport report = reportRepository.findWithItemsById(reportId)

            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

    // sécurité
    if (!report.getEmployee().getId().equals(user.getId())) {
        throw new BusinessException("You cannot delete this report");
    }

    if (report.getStatus() != ExpenseStatus.DRAFT) {
        throw new BusinessException("Only DRAFT reports can be deleted");
    }

    reportRepository.delete(report);
}


    // ======================
    // MANAGER ACTIONS
    // ======================
 @Transactional
public ExpenseReport approve(Long reportId, String comment) {

    // ⭐⭐⭐ LA CORRECTION EST ICI
    ExpenseReport report = reportRepository.findFullReport(reportId)

            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

    if (report.getStatus() != ExpenseStatus.SUBMITTED) {
        throw new BusinessException("Only SUBMITTED reports can be approved");
    }

    report.setStatus(ExpenseStatus.APPROVED);
    report.setManagerComment(
            comment != null && !comment.isBlank()
                    ? comment
                    : "Approved by manager"
    );

    // maintenant Hibernate connaît déjà les items
    for (Expense i : report.getItems()) {
        i.setStatus(ExpenseStatus.APPROVED);
    }

    return reportRepository.save(report);
}




@Transactional
public ExpenseReport reject(Long reportId, String comment) {

    ExpenseReport report = reportRepository.findFullReport(reportId)

            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

    if (report.getStatus() != ExpenseStatus.SUBMITTED) {
        throw new BusinessException("Only SUBMITTED reports can be rejected");
    }

    report.setStatus(ExpenseStatus.REJECTED);
    report.setManagerComment(
            comment != null && !comment.isBlank()
                    ? comment
                    : "Rejected by manager"
    );

    for (Expense i : report.getItems()) {
        i.setStatus(ExpenseStatus.REJECTED);
    }

    return reportRepository.save(report);
}


    // ======================
    // MY REPORTS (JWT)
    // ======================
    @Transactional(readOnly = true)
    public Page<ExpenseReport> myReports(String email, int page, int size) {

        User user = getUserByEmail(email);

        return reportRepository.findByEmployeeId(
                user.getId(),
                PageRequest.of(page, size)
        );
    }

    // ======================
    // PENDING REPORTS
    // ======================

@Transactional(readOnly = true)
public Page<ExpenseReport> pending(int page, int size) {

    return reportRepository.findByStatus(
            ExpenseStatus.SUBMITTED,
            PageRequest.of(page, size)
    );
}



@Transactional(readOnly = true)
public ExpenseReportResponse getReportForManager(Long id) {

    ExpenseReport report = reportRepository.findFullReport(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

    return ExpenseMapper.toReportResponse(report);
}




    @Transactional(readOnly = true)
public Map<String, Long> getStats() {

    Map<String, Long> stats = new HashMap<>();

    stats.put("pending",
            reportRepository.countByStatus(ExpenseStatus.SUBMITTED));

    stats.put("approved",
            reportRepository.countByStatus(ExpenseStatus.APPROVED));

    stats.put("rejected",
            reportRepository.countByStatus(ExpenseStatus.REJECTED));

    stats.put("paid",
            reportRepository.countByStatus(ExpenseStatus.PAID));

    return stats;
}

    // ======================
    // PAY
    // ======================
    @Transactional
    public ExpenseReport pay(Long reportId) {

        ExpenseReport report = reportRepository.findFullReport(reportId)


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


@Transactional(readOnly = true)
public Page<ExpenseReport> findByStatus(
        ExpenseStatus status,
        int page,
        int size
) {
    return reportRepository.findByStatus(
            status,
            PageRequest.of(page, size)
    );
}

}
