package org.example.module_5_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class Module5ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Module5ConsumerApplication.class, args);
    }

}
