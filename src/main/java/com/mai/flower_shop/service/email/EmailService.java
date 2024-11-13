package com.mai.flower_shop.service.email;

import com.mai.flower_shop.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendVerificationEmail(String recipientEmail, String token) throws MessagingException {
        String subject = "Email Verification";
        String message2 = "Your verification code is: " + token ;
        String confirmationUrl = "http://localhost:2003/api/v1/auth/verify?token=" + token;
        String message = message2 + "\n Please click the link below to verify your email:\n" + confirmationUrl;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(message, true);
        mailSender.send(mimeMessage);
    }
}