package com.openfinance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openApiSpec()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Consent Service API")
                        .version("1.0")
                        .description("Documentação da API de consentimentos."));
    }
}
