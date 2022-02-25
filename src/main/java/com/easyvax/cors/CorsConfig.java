package com.easyvax.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * In questa classe vengono gestiti i CORS (cross-origin HTTP request)
 * per l'interfacciamento con il front-end.
 * Per avere maggiori informazioni su cosa sono i CORS guardare la documentazioni.
 */

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer getCorsConfiguration(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080", "http://192.168.1.57:3000")
                        .allowedMethods("PUT","POST","GET","DELETE");
            }
        };
    }
}
