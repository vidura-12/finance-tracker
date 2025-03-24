package com.example.finance_tracker.controllers;

import com.example.finance_tracker.service.ReportService;
import com.example.finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")

public class ReportController {
    private final TransactionService transactionService;
    private final ReportService reportService;

    public ReportController(TransactionService transactionService, ReportService reportService) {
        this.transactionService = transactionService;
        this.reportService = reportService;
    }

    @GetMapping("/{userId}/monthly-report")
    public ResponseEntity<byte[]> downloadMonthlyReport(@PathVariable String userId) {
        List<Map<String, Object>> transactions = transactionService.getIncomeVsExpenseTrends(userId);
        byte[] pdfBytes = reportService.generateMonthlyReport(transactions);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monthly_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    @GetMapping("/{userId}/monthly-report/csv")
    public ResponseEntity<byte[]> downloadCSVReport(@PathVariable String userId) {
        List<Map<String, Object>> transactions = transactionService.getIncomeVsExpenseTrends(userId);
        String csvData = reportService.generateCSVReport(transactions);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monthly_report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csvData.getBytes());
    }

}
