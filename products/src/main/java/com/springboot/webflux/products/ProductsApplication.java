package com.springboot.webflux.products;

import com.springboot.webflux.products.config.AllDBsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 *
 * @author Mario Ruiz Rojo
 * Main application web service runner
 * It sets AllDBsProperties as properties reader
 */
@EnableWebFlux
@SpringBootApplication
@EnableConfigurationProperties( AllDBsProperties.class)
public class ProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsApplication.class, args);
	}

}
