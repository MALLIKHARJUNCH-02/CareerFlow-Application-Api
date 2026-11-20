package com.careerflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Swagger UI settings. Adds an "Authorize" button so you can paste your JWT. */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI careerFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CareerFlow API").version("1.0")
                        .description("Student Job Application Tracker"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT")));
    }
}
