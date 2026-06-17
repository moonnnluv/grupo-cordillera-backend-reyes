package com.grupcordillera.msbff.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${MS_DATOS_URL}")
    private String msDatosUrl;

    @Value("${MS_KPI_URL}")
    private String msKpiUrl;

    @Value("${MS_REPORTES_URL}")
    private String msReportesUrl;

    private WebClient buildClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .responseTimeout(Duration.ofMillis(5000));
        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Bean
    public WebClient msDatosClient() {
        return buildClient(msDatosUrl);
    }

    @Bean
    public WebClient msKpiClient() {
        return buildClient(msKpiUrl);
    }

    @Bean
    public WebClient msReportesClient() {
        return buildClient(msReportesUrl);
    }
}