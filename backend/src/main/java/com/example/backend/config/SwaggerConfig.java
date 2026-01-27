package com.example.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Test APIs")
                        .version("1.0")
                        .description("<h2>Don't forget to add the token to authorize requests.</h2> <br> Only the /register and /login endpoints are public. <br> For other endpoints, click on the 'Authorize' button and enter your the JWT tokn."))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", 
                            new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token")
                        ));
    }
}