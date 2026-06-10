package com.grupcordillera.mskpi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msKpiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-KPI API")
                        .description("Microservicio de gestión de KPIs de Grupo Cordillera. " +
                                "Permite calcular, registrar y consultar indicadores clave de rendimiento por tipo y sucursal.")
                        .version("1.0.0"));
    }
}
