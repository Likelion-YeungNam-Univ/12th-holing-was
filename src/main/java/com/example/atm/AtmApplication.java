package com.example.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AtmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtmApplication.class, args);
	}

}
