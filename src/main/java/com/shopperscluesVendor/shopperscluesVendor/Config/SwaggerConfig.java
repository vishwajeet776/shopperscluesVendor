package com.shopperscluesVendor.shopperscluesVendor.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API Documentation")
                        .version("1.0.0")
                        .description("This Swagger UI provides all endpoints of the User Service microservice.")
                        .contact(new Contact()
                                .name("Rutwik Balkhande")
                                .email("rutwik@company.com")
                                .url("https://github.com/rutwik-balkhande")));
    }
}
