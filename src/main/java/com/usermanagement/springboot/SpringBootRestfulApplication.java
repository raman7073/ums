package com.usermanagement.springboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBootRestfulApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringBootRestfulApplication.class, args);
		System.out.println("Welcome to Office");

	}

}
