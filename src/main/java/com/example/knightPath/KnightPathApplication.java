package com.example.knightPath;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KnightPathApplication implements ApplicationRunner {

	private final KnightService knightService;

	public KnightPathApplication(KnightService knightService) {
		this.knightService = knightService;
	}

	public static void main(String[] args) {
		SpringApplication.run(KnightPathApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
			knightService.main(args);
	}
}
