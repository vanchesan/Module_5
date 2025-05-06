package org.example.module_5_consumer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.module_5_consumer.DTO.UserDTO;
import org.example.module_5_consumer.Service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/notify")
@RequiredArgsConstructor
public class EmailSenderController {

    private final NotificationService notificationService;


    @PostMapping
    public void send(@RequestBody UserDTO userDTO) {
        notificationService.sendNotification(userDTO.getUser(), userDTO.getOperation());
    }
}
