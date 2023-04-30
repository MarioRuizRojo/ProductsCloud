package com.springboot.webflux.products.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.webflux.products.Fixtures;
import com.springboot.webflux.products.config.AdminAuthenticator;
import com.springboot.webflux.products.config.CustomSecurity;
import com.springboot.webflux.products.model.ResponseUtils;
import com.springboot.webflux.products.model.dto.ProductDTO;
import com.springboot.webflux.products.model.dto.ResponseDTO;
import com.springboot.webflux.products.service.EmployeeService;
import com.springboot.webflux.products.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ProductController.class)
@Import({ProductService.class, EmployeeService.class, 
    CustomSecurity.class, AdminAuthenticator.class})
@ExtendWith( SpringExtension.class )
@ActiveProfiles(value="test")
public class ProductControllerTest {
    @Autowired
    private Environment environment;

    @Value("${config.urlCategories}")
    private String CATEGORIES_URL;

    @Value("${config.urlProducts}")
    private String PRODUCTS_URL;
    
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ProductService productService;

    @MockBean
    private EmployeeService employeeService;

    private String productsBaseURL;

    @BeforeEach
	public void setup() {
        this.productsBaseURL = "/"+PRODUCTS_URL+"/";
        when(employeeService.getAll()).thenReturn(Flux.just(null,null));    
    }

    //------------------------OK-----------------------------
    @Test
    void product_list_2products(){
        when(productService.getAll()).thenReturn(Mono.just(Fixtures.response2products()));
        assertEquals("test",environment.getActiveProfiles()[0]);
        webClient.get()
        .uri(productsBaseURL)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().size()==2);
        });
    }

    @Test
    void category_list_2categories(){
        when(productService.getAllCategories()).thenReturn(Mono.just(Fixtures.response2categories()));
        webClient.get()
        .uri(productsBaseURL+CATEGORIES_URL)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getCategoriesDTO().size()==2);    
        });
    }

    @Test
    void product_getById_ok(){
        when(productService.getById(anyString())).thenReturn(Mono.just(Fixtures.responseOneProduct()));
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(productsBaseURL+"{id}")
            .build(Fixtures.objectId().toString())
        )
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().size()==1);
        });
    }

    @Test
    void product_insertNew_ok(){
        when(productService.insertNew(any())).thenReturn(Mono.just(Fixtures.responseOneProduct()));
        webClient.post()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(Fixtures.oneProduct(LocalDateTime.now())), ProductDTO.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().size()==1);
        });
    }

    @Test
    void product_update_ok(){
        when(productService.update(any())).thenReturn(Mono.just(Fixtures.responseOneProduct()));
        webClient.put()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(Fixtures.oneProduct(LocalDateTime.now())), ProductDTO.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().size()==1);
        });
    }

    @Test
    void product_delete_ok(){
        when(productService.delete(anyString())).thenReturn(Mono.just(ResponseUtils.emptyResponse()));
        webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path(productsBaseURL+"{id}")
            .build(Fixtures.objectId().toString())
        )
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().isEmpty());
            assertTrue(responseDTO.getErrorMsg().isEmpty());
        });
    }
}
