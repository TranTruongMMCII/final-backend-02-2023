package com.r2s.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com")
public class JavaBackend02Application {

	public static void main(String[] args) {
		SpringApplication.run(JavaBackend02Application.class, args);
	}

}
