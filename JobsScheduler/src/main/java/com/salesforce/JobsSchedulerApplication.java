package com.salesforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JobsSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsSchedulerApplication.class, args);
	}

}
