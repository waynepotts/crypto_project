package com.wayne.restservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.wayne.entities.jpa")
@EntityScan("com.wayne.entities.jpa")
public class RestservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestservicesApplication.class, args);
	}

}
