package com.ayon.api.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ayon.api.stats")
public class StatisticsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatisticsApiApplication.class, args);
	}
}
