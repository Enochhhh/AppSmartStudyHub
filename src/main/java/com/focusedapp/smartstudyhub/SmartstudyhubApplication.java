package com.focusedapp.smartstudyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SmartstudyhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartstudyhubApplication.class, args);
	}
	
	@Bean
    WebMvcConfigurer corsConfigurer()
	{
		return new WebMvcConfigurer() {
			@SuppressWarnings("unused")
			public void addCorsMapping(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000");
			}
		};
	}
}
