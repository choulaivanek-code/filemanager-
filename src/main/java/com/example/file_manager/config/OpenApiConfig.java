package com.example.file_manager.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SOA File Manager API",
                version = "1.0",
                description = "Gestion des fichiers — projet SOA"
        )
)
public class OpenApiConfig {
}

