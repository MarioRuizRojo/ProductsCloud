package com.springboot.webflux.productscurrency.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.webflux.productscurrency.model.dto.ProductDTO;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;

import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * CProducts web client
 * It makes requests to the products API
 */
@Service
public class ProductsClient {    

    /**
     * Relative path URL to categories of products endpoint
     */
    private final String URL_CATEGORIES_ENDPOINT;

    /**
	 * Web Client for REST api products
	 */
	private WebClient webClient;

    /**
     * Setup absolute path URL to products external service
     */
    public ProductsClient(
        @Value("${external.products.url}") String urlProductsService,
        @Value("${external.categories.url}") String urlCategoriesEndpoint
        ){
            this.URL_CATEGORIES_ENDPOINT = urlCategoriesEndpoint;
            this.webClient = WebClient.builder().baseUrl(urlProductsService).build();
    }

    /**
     * It requests all products
     * @return response with list of products
     */
    public Mono<ResponseDTO> getAll() {
        return webClient.get()
            .accept(MediaType.APPLICATION_JSON)
		    .retrieve()
		    .bodyToMono(ResponseDTO.class);
    }

    /**
     * It requests all categories
     * @return response with list of products
     */
    public Mono<ResponseDTO> getAllCategories() {
        return webClient.get()
            .uri(URL_CATEGORIES_ENDPOINT)
            .accept(MediaType.APPLICATION_JSON)
		    .retrieve()
		    .bodyToMono(ResponseDTO.class);
    }

    /**
     * It requests one product by id
     * @return response with one product inside the list of products
     */
    public Mono<ResponseDTO> getById(String id) {
        return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("{id}")
            .build(id)
        )
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(ResponseDTO.class);
    }

    /**
     * It requests to insert one product
     * @return response with one product inside the list of products
     */
    public Mono<ResponseDTO> insertNew(ProductDTO productDTO) {
        return webClient.post()
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(productDTO, ProductDTO.class)
        .retrieve()
        .bodyToMono(ResponseDTO.class);
    }

    /**
     * It requests to update one product
     * @return response with one product inside the list of products
     */
    public Mono<ResponseDTO> update(ProductDTO productDTO) {
        return webClient.put()
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(productDTO, ProductDTO.class)
        .retrieve()
        .bodyToMono(ResponseDTO.class);
    }

    /**
     * It requests to delete one product by id
     * @return ack response
     */
    public Mono<ResponseDTO> delete(String id) {
        return webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path("{id}")
            .build(id)
        )
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(ResponseDTO.class);
    }    
}
