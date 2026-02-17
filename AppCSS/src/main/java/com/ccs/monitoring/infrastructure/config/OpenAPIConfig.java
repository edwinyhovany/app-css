package com.ccs.monitoring.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI ccsOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("CCS Vehicle Monitoring API")
                .description("Sistema de Monitoreo y Seguimiento de Vehículos")
                .version("1.0.0")
                .contact(new Contact()
                    .name("CCS Development Team")
                    .email("dev@ccs.com")));
    }
}
