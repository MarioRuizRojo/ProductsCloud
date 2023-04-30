package com.springboot.webflux.products.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * 
 * @author Mario Ruiz Rojo
 * It setups the relationship between Employees DB adaptor and EmployeeDB repositories
 */
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.springboot.webflux.products.repository.employeedb",
        reactiveMongoTemplateRef = "employeeTemplate")
public class EmployeeDBConfig {
}