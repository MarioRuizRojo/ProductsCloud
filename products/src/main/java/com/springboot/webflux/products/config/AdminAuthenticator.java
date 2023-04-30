package com.springboot.webflux.products.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mario Ruiz Rojo
 * It manages the authentication process of the administrator user 
 */
@Component
public class AdminAuthenticator {

    /**
     * Admin password from properties
     */
    @Value("${admin.masterkey:-}")
    private String AdminPassword;

    /**
     * Authentication function
     * @return authentication function
     */
    public ReactiveAuthenticationManager getAuthenticator(){
        return this::authenticate;
    }

    /**
     * Admin user identifier
     * @return admin role
     */
    public GrantedAuthority role(){
        return new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "admin";
            }
        };
    }

    /**
     * Admin authentication function
     * @param authentication
     * @return auth token
     */
    public Mono<Authentication> authenticate(Authentication authentication) {
        String enteredPassword = (String) authentication.getCredentials();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(this.role());
        var token = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                roles
        );
        if(enteredPassword.equals(this.AdminPassword)){
            return Mono.just(token);
        }
        token.setAuthenticated(false);
        return Mono.just(authentication);
    }
}
