package com.example.finance_tracker.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBudgetWarning(String to, String category, String userId, double spent, double limit) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);  // Use dynamic 'to' instead of hardcoded email
            helper.setSubject("⚠ Budget Warning: " + category);
            helper.setText("""
                Hello,
                
                You have reached 80% of your budget for category: %s.
                - Budget Limit: $%.2f
                - Spent: $%.2f
                
                Consider adjusting your expenses.
                
                Regards,
                Finance Tracker Team
            """.formatted(category, limit, spent), false);

            mailSender.send(message);
            logger.info("📧 Budget warning email sent to: {}", to);

        } catch (Exception e) {
            logger.error("❌ Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    public void sendEmail(String userId, String subject, String messageContent) {
        try {
            MimeMessage message1 = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message1, true);

            helper.setTo("viduranirmal@gmail.com");  // Use the dynamic 'userId' or any other recipient
            helper.setSubject(subject);
            helper.setText(messageContent);  // Correctly set the 'messageContent' as the email body

            mailSender.send(message1);
            logger.info("📧 Email sent to: {}", "viduranirmal@gmail.com");

        } catch (Exception e) {
            logger.error("❌ Failed to send email: {}", e.getMessage());
        }
    }
}
