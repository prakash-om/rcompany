package com.raksul.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
//exclude = {DataSourceAutoConfiguration.class }
public class RaksulApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaksulApplication.class, args);
	}

}
