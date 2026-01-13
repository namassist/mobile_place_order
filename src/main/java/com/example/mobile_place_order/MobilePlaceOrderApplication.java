package com.example.mobile_place_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MobilePlaceOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobilePlaceOrderApplication.class, args);
	}

}