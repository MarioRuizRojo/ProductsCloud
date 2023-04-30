package com.springboot.webflux.productscurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 *
 * @author Mario Ruiz Rojo
 * Main application web service runner
 */
@EnableWebFlux
@SpringBootApplication
public class ProductscurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductscurrencyApplication.class, args);
	}

}
