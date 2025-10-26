package com.arun.ytclone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${auth0.audience:http://localhost:8080}")
    private String audience;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearer-jwt";
        
        return new OpenAPI()
                .info(new Info()
                        .title("YouTube Clone API")
                        .version("1.0.0")
                        .description("A comprehensive REST API for a YouTube clone application built with Spring Boot and MongoDB. " +
                                "This API provides endpoints for video management, user authentication, comments, likes/dislikes, and subscriptions.")
                        .contact(new Contact()
                                .name("Arun Sakthi Anand M")
                                .email("arunsakthianand@gmail.com")
                                .url("https://github.com/ArunSakthiAnandM"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtained from Auth0")));
    }
}
