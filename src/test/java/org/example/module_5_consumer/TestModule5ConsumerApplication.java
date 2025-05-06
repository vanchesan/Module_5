package org.example.module_5_consumer;

import org.springframework.boot.SpringApplication;

public class TestModule5ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.from(Module5ConsumerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
