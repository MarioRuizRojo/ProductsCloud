package com.springboot.webflux.products.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 *
 * @author Mario Ruiz Rojo
 * It implements access rules on each endpoint of the web service
 * Performs login using admin credentials
 * No security for dev profile
 */
@Configuration
@EnableWebFluxSecurity()
public class CustomSecurity {

    /**
     * Admin user authentication process implementation
     */
    private final AdminAuthenticator adminAuthenticator;

    public CustomSecurity(AdminAuthenticator adminAuthenticator){
        this.adminAuthenticator = adminAuthenticator;
    }

    /**
     * Access rule set:
     * /actuator/health 
     *      is open
     * /products/** and /actuator/** 
     *      require admin user to login
     *      it opens the spring basic login popup
     * @param http
     * @return filter chain
     * @throws Exception
     */
    @Bean
    @Profile({"qa","production"})
    public SecurityWebFilterChain apiFilterChain(ServerHttpSecurity http) throws Exception {
        return http.csrf().disable()
                // no basic auth for health endpoint
                .authorizeExchange().pathMatchers("/actuator/health").permitAll()
                .and()
                //basic auth for contacts and actuators
                .authorizeExchange().pathMatchers("/products/**","/actuator/**").authenticated().and().httpBasic()
                .disable().addFilterAfter(
                        new AuthenticationWebFilter(this.adminAuthenticator.getAuthenticator()),
                        SecurityWebFiltersOrder.REACTOR_CONTEXT
                )
                .build();
    }

    @Bean
    @Profile({"dev","test"})
    public SecurityWebFilterChain noSecurity(ServerHttpSecurity http) throws Exception {
        return http.csrf().disable()
        .authorizeExchange().pathMatchers("/**").permitAll()
        .and()
        .build();
    }
}
