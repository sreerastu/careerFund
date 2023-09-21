package com.example.Foundation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.foundation")
public class FoundationApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoundationApplication.class, args);
	}

}
