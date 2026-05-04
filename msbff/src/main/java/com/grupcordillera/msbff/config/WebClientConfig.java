package com.grupcordillera.msbff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${MS_DATOS_URL}")
    private String msDatosUrl;

    @Value("${MS_KPI_URL}")
    private String msKpiUrl;

    @Value("${MS_REPORTES_URL}")
    private String msReportesUrl;

    // Un WebClient por microservicio — cada uno apunta a su URL base
    @Bean
    public WebClient msDatosClient() {
        return WebClient.builder().baseUrl(msDatosUrl).build();
    }

    @Bean
    public WebClient msKpiClient() {
        return WebClient.builder().baseUrl(msKpiUrl).build();
    }

    @Bean
    public WebClient msReportesClient() {
        return WebClient.builder().baseUrl(msReportesUrl).build();
    }
}