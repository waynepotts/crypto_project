package com.wayne.restservices;

import com.wayne.restservices.config.CoinGeckoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(
		CoinGeckoProperties.class
)
@EnableJpaRepositories("com.wayne.restservices.repositories")
@EntityScan("com.wayne.restservices.entities.jpa")
@EnableAsync
public class RestservicesApplication {


	public static void main(String[] args) {
		SpringApplication.run(RestservicesApplication.class, args);
	}

}
