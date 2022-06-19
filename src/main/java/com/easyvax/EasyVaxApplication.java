package com.easyvax;


import com.easyvax.model.Utente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Locale;

@SpringBootApplication
public class EasyVaxApplication {


    public static void main(String[] args) {
        SpringApplication.run(EasyVaxApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    public class StaticResourcesConfigurer implements WebMvcConfigurer {
        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/resources/static/**").addResourceLocations("classpath:/resources/static/");
        }
    }


}
