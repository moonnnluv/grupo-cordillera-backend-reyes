package com.grupcordillera.msdatos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msDatosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-Datos API")
                        .description("Microservicio de gestión de datos organizacionales de Grupo Cordillera. " +
                                "Permite registrar, consultar y eliminar datos por fuente y sucursal.")
                        .version("1.0.0"));
    }
}
