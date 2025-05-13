package org.example.module_5_consumer;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.example.module_5_consumer.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@EmbeddedKafka
@SpringBootTest(properties = {
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@ActiveProfiles("test")
public class NotificationConsumerIntegrationTest {

    private static final String USER_EMAIL = "test@test.ru";
    private static final String TOPIC       = "user-events";
    private static final String OPERATION   = "create";

    @Autowired
    private KafkaTemplate<String, UserDTO> kafkaTemplate;

    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(
                            GreenMailConfiguration.aConfig().withUser("test", "test")
                    )
                    .withPerMethodLifecycle(false);

    @Test
    void whenKafkaMessageArrives_thenEmailServiceIsCalled() throws Exception {

        kafkaTemplate.send(TOPIC, new UserDTO(USER_EMAIL, OPERATION));

        await().atMost(Duration.ofSeconds(10))
                .until(() -> greenMail.getReceivedMessages().length > 0);


        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertThat(messages).hasSize(1);

        MimeMessage message = messages[0];
        assertThat(message.getAllRecipients()[0].toString())
                .isEqualTo(USER_EMAIL);
        assertThat(message.getSubject()).isEqualTo("Добро пожаловать");

    }
}
