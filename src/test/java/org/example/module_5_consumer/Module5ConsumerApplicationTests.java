package org.example.module_5_consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Module5ConsumerApplicationTests {

    @Test
    void contextLoads() {
    }

}
