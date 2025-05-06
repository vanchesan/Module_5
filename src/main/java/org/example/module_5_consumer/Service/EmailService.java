package org.example.module_5_consumer.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor

public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @SneakyThrows
    public void sendMail(String to, String subject, String operation) {
        Context context = new Context();
        context.setVariable("userEmail", to);
        String processed = templateEngine.process(operation, context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(processed, true);

        mailSender.send(mimeMessage);
        System.out.println("Email sent " + to);
    }
}
