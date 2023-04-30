package com.springboot.webflux.products.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * 
 * @author Mario Ruiz Rojo
 * It setups the relationship between Products DB adaptor and ProductDB repositories
 */
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.springboot.webflux.products.repository.productdb",
        reactiveMongoTemplateRef = "productTemplate")
public class ProductDBConfig {
}