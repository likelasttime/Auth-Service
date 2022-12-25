package com.example.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class FrontServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontServerApplication.class, args);
	}

}