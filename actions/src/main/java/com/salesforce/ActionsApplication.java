package com.salesforce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@OpenAPIDefinition
@EnableFeignClients
@EnableScheduling
@CrossOrigin("*")
public class ActionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActionsApplication.class, args);
	}

}
