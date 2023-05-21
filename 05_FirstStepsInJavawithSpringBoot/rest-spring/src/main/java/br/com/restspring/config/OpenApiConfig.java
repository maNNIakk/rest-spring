package br.com.restspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("RESTful API with JavaSE-17 and Spring Boot 3")
						.version("v1")
						.description("Some cool description about the API")
						.termsOfService("https://github.com/maNNIakk/rest-spring")
						.license(
								new License()
								.name("Apache 2.0")
								.url("https://github.com/maNNIakk/rest-spring")));
	}
	
}
