package com.home.ledclockservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class LedclockserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedclockserviceApplication.class, args);
	}

}
