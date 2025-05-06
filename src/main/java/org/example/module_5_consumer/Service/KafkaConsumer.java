package org.example.module_5_consumer.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.example.module_5_consumer.DTO.UserDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmailService emailService;
    private final NotificationService notificationService;


    @KafkaListener(topics = "${kafka.topic}", groupId = "my-group")
    public void listen(String message) {
        UserDTO userDTO = null;

        try {
            userDTO = objectMapper.readValue(message, UserDTO.class);
            notificationService.sendNotification(userDTO.getUser(), userDTO.getOperation());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

}
