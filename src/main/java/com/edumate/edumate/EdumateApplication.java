package com.edumate.edumate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EdumateApplication {
	public static void main(String[] args) {
		SpringApplication.run(EdumateApplication.class, args);
	}
}
