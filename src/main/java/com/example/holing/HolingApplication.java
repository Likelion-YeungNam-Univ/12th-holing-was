package com.example.holing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HolingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HolingApplication.class, args);
    }

}
