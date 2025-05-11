package org.example.module_5_consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.Awaitility;
import org.example.module_5_consumer.Service.NotificationService;
import org.example.module_5_consumer.Service.EmailService;
import org.example.module_5_consumer.DTO.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
public class NotificationConsumerIntegrationTest {

    private static final String TOPIC = "user-events";

    @Container
    static KafkaContainer kafka = new KafkaContainer("7.5.0")
            .withEmbeddedZookeeper();

    @DynamicPropertySource
    static void configureKafka(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private EmailService emailService;          // Мокируем отправку писем

    @Autowired
    private ObjectMapper objectMapper;          // Для конвертации DTO в JSON

    @Test
    void whenKafkaMessageArrives_thenEmailServiceIsCalled() throws Exception {
        // Подготовим DTO и JSON
        UserDTO dto = new UserDTO("test@example.com", "create");
        String json = objectMapper.writeValueAsString(dto);

        // Отправляем сообщение в Kafka
        kafkaTemplate.send(TOPIC, json);

        // Ждём, пока слушатель обработает сообщение
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(200))
                .untilAsserted(() ->
                        // Проверяем, что на слой EmailService пришёл вызов
                        verify(emailService)
                                .sendMail(eq("test@example.com"),
                                        eq("Добро пожаловать"),
                                        eq("user_create"))
                );
    }
}
