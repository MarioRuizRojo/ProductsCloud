package com.springboot.webflux.products.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Mario Ruiz Rojo
 * It reads Mongo databases parameters from properties
 */
@Configuration
@ConfigurationProperties(prefix="spring.data.mongodb")
@Getter
@Setter
public class AllDBsProperties {
    /**
     * Products DB, primary
     */
    private MongoProperties products;
    /**
     * Employees DB
     */
    private MongoProperties employees;
}
