package com.im.chats;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.logging.Filter;

@SpringBootApplication
public class ChatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatsApplication.class, args);
    }


}
