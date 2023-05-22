package com.home.ledclockservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LedClockServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LedClockServiceApplication.class, args);
	}

}
