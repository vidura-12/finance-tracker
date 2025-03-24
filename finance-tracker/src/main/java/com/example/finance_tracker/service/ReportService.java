package com.example.finance_tracker.service;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.*;
@Service
public class ReportService {

    public byte[] generateMonthlyReport(List<Map<String, Object>> transactions) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new  PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Monthly Financial Report"));
            document.add(new Paragraph("===================================="));

            for (Map<String, Object> data : transactions) {
                Map<String, Object> id = (Map<String, Object>) data.get("_id");
                document.add(new Paragraph(
                        "Month: " + id.get("month") + "/" + id.get("year") +
                                " | Income: $" + data.get("totalIncome") +
                                " | Expense: $" + data.get("totalExpense")
                ));
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
    public String generateCSVReport(List<Map<String, Object>> transactions) {
        try (StringWriter out = new StringWriter(); CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("Month", "Year", "Total Income", "Total Expense"))) {

            for (Map<String, Object> data : transactions) {
                Map<String, Object> id = (Map<String, Object>) data.get("_id");
                csvPrinter.printRecord(id.get("month"), id.get("year"), data.get("totalIncome"), data.get("totalExpense"));
            }

            csvPrinter.flush();
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }
}
