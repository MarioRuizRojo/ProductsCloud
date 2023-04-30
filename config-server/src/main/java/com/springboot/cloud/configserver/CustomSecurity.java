package com.springboot.cloud.configserver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomSecurity {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf()
        .ignoringRequestMatchers("/encrypt/**")
        .ignoringRequestMatchers("/decrypt/**")
        .and()
        .build();
    }
}