package com.example.backend.serviceImpl;

import com.example.backend.model.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Autowired
    private TemplateEngine templateEngine;

    public void sendOrderConfirmationEmail(String toEmail, Order order) throws MessagingException {
        // Tạo một MimeMessage
        Context context = new Context();
        context.setVariable("orderId", order.getId());
        context.setVariable("orderAmount", order.getOrderAmount());
        context.setVariable("orderStatus", order.getOrderStatus());

        // Render template HTML thành nội dung email
        String emailContent = templateEngine.process("order-confirmation", context);

        // Gửi email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(toEmail);
        helper.setSubject("Xác nhận đơn hàng #" + order.getId());
        helper.setText(emailContent, true);
        helper.setFrom(fromEmail);

        mailSender.send(message);
    }

    public void sendOrderStatusUpdateEmail(String toEmail, Order order) {
        try {
            // Thiết lập context cho Thymeleaf
            Context context = new Context();
            context.setVariable("orderId", order.getId());
            context.setVariable("orderStatus", order.getOrderStatus());

            // Render template thành HTML
            String emailContent = templateEngine.process("update-orderStatus", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setTo(toEmail);
            helper.setSubject("Cập nhật đơn hàng #" + order.getId());
            helper.setText(emailContent, true); // true: nội dung là HTML
            helper.setFrom(fromEmail);

            mailSender.send(message);
        } catch (MessagingException e) {
            // Log lỗi hoặc xử lý theo yêu cầu nghiệp vụ
            throw new RuntimeException("Lỗi khi gửi email cập nhật trạng thái đơn hàng", e);
        }
    }
}


