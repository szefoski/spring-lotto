package com.daniel.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@Configuration
@EnableScheduling
public class LottoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LottoApplication.class, args);
	}

}
