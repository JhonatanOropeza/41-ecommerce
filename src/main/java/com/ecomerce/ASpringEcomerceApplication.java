package com.ecomerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ASpringEcomerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ASpringEcomerceApplication.class, args);
	}

}
