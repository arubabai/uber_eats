package com.uber.eats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableJpaRepositories
@PropertySource(value = "messages.properties")
@OpenAPIDefinition(info = @Info(title = "Uber Eats Service", version = "1.0", description = "Uber Eats Service"))
public class UberEatsApplication {

	public static void main(String[] args) {

		SpringApplication.run(UberEatsApplication.class, args);

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {

		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/**");

			}

		};

	}

	@Bean
	public CookieSerializer cookieSerializer() {

		DefaultCookieSerializer serializer = new DefaultCookieSerializer();

		serializer.setCookieName("Session");
		serializer.setSameSite("Lax");

		return serializer;
	}

}
