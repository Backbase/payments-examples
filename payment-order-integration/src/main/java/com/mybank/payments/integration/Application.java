package com.mybank.payments.integration;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

// tag::add-import-annotation[]
@SpringBootApplication
@EnableDiscoveryClient
@Import({PaymentOrdersController.class, OrderExecutor.class })
@EnableScheduling
public class Application extends SpringBootServletInitializer {
// end::add-import-annotation[]

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ObjectWriter objectWriter(ObjectMapper objectMapper){
        return objectMapper.writer(new DefaultPrettyPrinter());
    }

}