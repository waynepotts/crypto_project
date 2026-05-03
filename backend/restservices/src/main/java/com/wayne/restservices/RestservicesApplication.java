package com.wayne.restservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.wayne.restservices.repositories")
@EntityScan("com.wayne.restservices.entities.jpa")
public class RestservicesApplication {


	public static void main(String[] args) {
		SpringApplication.run(RestservicesApplication.class, args);
	}

}
