package com.springboot.webflux.productscurrency.controller;

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

import com.springboot.webflux.productscurrency.Fixtures;
import com.springboot.webflux.productscurrency.config.AdminAuthenticator;
import com.springboot.webflux.productscurrency.config.CustomSecurity;
import com.springboot.webflux.productscurrency.model.ResponseUtils;
import com.springboot.webflux.productscurrency.model.dto.ProductDTO;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;
import com.springboot.webflux.productscurrency.service.ProductService;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ProductController.class)
@Import({ProductService.class, 
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

    private String productsBaseURL;

    private LocalDateTime now;

    @BeforeEach
	public void setup() {
        this.productsBaseURL = "/"+PRODUCTS_URL+"/";
        this.now = LocalDateTime.now();
    }

    //------------------------OK-----------------------------
    @Test
    void product_list_2products(){
        when(productService.getAll()).thenReturn(Fixtures.response2productsMono());
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
        when(productService.getAllCategories()).thenReturn(Fixtures.response2categoriesMono());
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
        when(productService.getById(anyString())).thenReturn(Fixtures.responseOneProductMono(now));
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(productsBaseURL+"{id}")
            .build(Fixtures.oneProductId())
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
        when(productService.insertNew(any())).thenReturn(Fixtures.responseOneProductMono(now));
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
        when(productService.update(any())).thenReturn(Fixtures.responseOneProductMono(now));
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
            .build(Fixtures.oneProductId())
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
