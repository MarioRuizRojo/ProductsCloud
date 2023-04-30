package com.springboot.webflux.products.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 *
 * @author Mario Ruiz Rojo
 * DBs configuration:
 * Two Mongo DBs conexions and one (mongo)document validator
 */
@Configuration
public class DBConfiguration {
    /**
     * All DBs parameters read from properties
     */
    private final AllDBsProperties allDBsProperties;

    public DBConfiguration(AllDBsProperties allDBsProperties) {
        this.allDBsProperties = allDBsProperties;
    }

    /**
     * MongoDB connexion to Products DB
     * @return connexion
     */
    @Primary
    @Bean
    public MongoClient productDBClient() {
        return MongoClients.create(MongoUtils.createMongoClientSettings(allDBsProperties.getProducts()));
    }

    /**
     * Mongo Adapter for Products DB into spring repository
     * @return adapter
     */
    @Primary
    @Bean("productTemplate")
    public ReactiveMongoTemplate productTemplate() {
        return new ReactiveMongoTemplate(productDBClient(),allDBsProperties.getProducts().getDatabase());
    }

    //second db conn
    /**
     * MongoDB connexion to Employee DB
     * @return connexion
     */
    @Bean
    public MongoClient employeeDBClient() {
        return MongoClients.create(MongoUtils.createMongoClientSettings(allDBsProperties.getEmployees()));
    }
    /**
     * Mongo Adapter for Employee DB into spring repository
     * @return adapter
     */
    @Bean("employeeTemplate")
    public ReactiveMongoTemplate employeeTemplate() {
        return new ReactiveMongoTemplate(employeeDBClient(),allDBsProperties.getEmployees().getDatabase());
    }

    //DAO validations
    /**
     * MongoDB document Validator
     * @return validator
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    /**
     * Basic validator
     * @return basic validator
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}