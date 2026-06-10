package com.grupcordillera.msreportes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msReportesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-Reportes API")
                        .description("Microservicio de visualización y gestión de reportes de Grupo Cordillera. " +
                                "Permite generar, consultar y eliminar reportes por tipo y sucursal.")
                        .version("1.0.0"));
    }
}
