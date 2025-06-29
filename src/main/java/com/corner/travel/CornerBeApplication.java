package com.corner.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.corner.travel")
public class CornerBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CornerBeApplication.class, args);
	}

}
