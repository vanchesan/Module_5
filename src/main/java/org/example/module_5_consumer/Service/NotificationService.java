package org.example.module_5_consumer.Service;

import lombok.*;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final EmailService emailService;

    public void sendNotification(String userEmail, String operation) {
        String template = operation.equalsIgnoreCase("create") ? "user_create" : "user_delete";
        String subject = operation.equalsIgnoreCase("create") ? "Добро пожаловать" : "Удаление аккаунта";
        emailService.sendMail(userEmail, subject, template);
    }
}
