package org.example.module_5_consumer.controller;

import lombok.*;

import org.example.module_5_consumer.dto.UserDTO;
import org.example.module_5_consumer.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class EmailSenderController {

    private final NotificationService notificationService;


    @PostMapping
    public void send(@RequestBody UserDTO userDTO) {
        notificationService.sendNotification(userDTO.getUser(), userDTO.getOperation());
    }
}
