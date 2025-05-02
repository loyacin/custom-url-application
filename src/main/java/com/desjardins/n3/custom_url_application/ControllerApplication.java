package com.desjardins.n3.custom_url_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ControllerApplication
		extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ControllerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ControllerApplication.class);
	}
}
