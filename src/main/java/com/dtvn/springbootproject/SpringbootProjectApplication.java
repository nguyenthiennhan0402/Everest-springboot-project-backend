package com.dtvn.springbootproject;

import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.utils.validators.PasswordValidator;
import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringbootProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootProjectApplication.class, args);
	}
}


