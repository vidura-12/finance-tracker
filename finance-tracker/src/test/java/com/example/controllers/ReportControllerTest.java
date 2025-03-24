package com.example.controllers;

import com.example.finance_tracker.controllers.ReportController;
import com.example.finance_tracker.service.ReportService;
import com.example.finance_tracker.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private List<Map<String, Object>> transactions;

    @BeforeEach
    public void setUp() {
        transactions = Collections.singletonList(Collections.singletonMap("month", "2023-10"));
    }

    // ✅ Test for downloadMonthlyReport
    @Test
    public void testDownloadMonthlyReport() {
        // Arrange
        byte[] pdfBytes = "PDF Content".getBytes();
        when(transactionService.getIncomeVsExpenseTrends("testuser")).thenReturn(transactions);
        when(reportService.generateMonthlyReport(transactions)).thenReturn(pdfBytes);

        // Act
        ResponseEntity<byte[]> response = reportController.downloadMonthlyReport("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals("attachment; filename=monthly_report.pdf", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(pdfBytes, response.getBody());
        verify(transactionService, times(1)).getIncomeVsExpenseTrends("testuser");
        verify(reportService, times(1)).generateMonthlyReport(transactions);
    }

    // ✅ Test for downloadCSVReport
    @Test
    public void testDownloadCSVReport() {
        // Arrange
        String csvData = "CSV Content";
        when(transactionService.getIncomeVsExpenseTrends("testuser")).thenReturn(transactions);
        when(reportService.generateCSVReport(transactions)).thenReturn(csvData);

        // Act
        ResponseEntity<byte[]> response = reportController.downloadCSVReport("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("attachment; filename=monthly_report.csv", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(csvData.getBytes(), response.getBody());
        verify(transactionService, times(1)).getIncomeVsExpenseTrends("testuser");
        verify(reportService, times(1)).generateCSVReport(transactions);
    }
}