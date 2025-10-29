package com.batuaa.transactionservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
@Slf4j
@Service
public class EmailService {
@Autowired
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
@Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendTransactionEmail(String toEmail, Map<String, Object> model) throws MessagingException {

        // Extract model variables safely
        String name = String.valueOf(model.getOrDefault("name", "User"));
        String transactionId = String.valueOf(model.getOrDefault("transactionId", "N/A"));
        String amount = String.valueOf(model.getOrDefault("amount", "0.00"));
        String date = String.valueOf(model.getOrDefault("date", "N/A"));
        String senderEmail = String.valueOf(model.getOrDefault("senderEmail", "N/A"));
        String receiverEmail = String.valueOf(model.getOrDefault("receiverEmail", "N/A"));
        String transactionType = String.valueOf(model.getOrDefault("transactionType", "N/A"));
        String status = String.valueOf(model.getOrDefault("status", "Pending"));

        // HTML email body using Java Text Block
        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Transaction Details</title>
                    <style>
                        body { font-family: Arial, sans-serif; color: #333; margin: 0; padding: 0; }
                        .container { 
                            background-color: #f9f9f9; 
                            padding: 20px; 
                            border-radius: 10px; 
                            max-width: 600px;
                            margin: 40px auto;
                            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                        }
                        h2 { color: #2b7a78; }
                        .highlight { color: #2b7a78; font-weight: bold; }
                        .status { 
                            display: inline-block; 
                            padding: 5px 10px; 
                            border-radius: 5px; 
                            color: white; 
                            background-color: %s;
                        }
                        ul { list-style: none; padding: 0; }
                        li { margin-bottom: 8px; }
                    </style>
                </head>
                <body>
                <div class="container">
                    <h2>Transaction Notification</h2>
                    <p>Dear <span class="highlight">%s</span>,</p>
                    <p>Your transaction has been successfully processed.</p>
                    <ul>
                        <li><strong>Transaction ID:</strong> %s</li>
                        <li><strong>Amount:</strong> ₹%s</li>
                        <li><strong>Date:</strong> %s</li>
                        <li><strong>Type:</strong> %s</li>
                        <li><strong>Status:</strong> <span class="status">%s</span></li>
                        <li><strong>From:</strong> %s</li>
                        <li><strong>To:</strong> %s</li>
                    </ul>
                    <p>Thank you for using our service!</p>
                </div>
                </body>
                </html>
                """.formatted(
                // Background color based on transaction status
                getStatusColor(status),
                name, transactionId, amount, date, transactionType, status, senderEmail, receiverEmail
        );

        //  Create MIME message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Transaction Details - " + status);
        helper.setText(htmlContent, true); // true = HTML content

        //  Send the email safely
        try {
            mailSender.send(message);
            log.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error(" Failed to send email: {}", e.getMessage());
            throw e;
        }
    }

    // 🎨 Helper method to choose status color dynamically
    private String getStatusColor(String status) {
        return switch (status.toLowerCase()) {
            case "success", "completed" -> "#28a745";   // green
            case "failed", "error" -> "#dc3545";       // red
            case "pending" -> "#ffc107";               // yellow
            default -> "#6c757d";                      // grey
        };
    }
}


