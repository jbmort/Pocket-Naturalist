package com.pocket.naturalist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NaturalistApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaturalistApplication.class, args);
	}

}
