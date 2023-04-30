package com.springboot.webflux.products;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.webflux.products.model.dto.ProductDTO;
import com.springboot.webflux.products.model.dto.ResponseDTO;
import com.springboot.webflux.products.repository.employeedb.CompanyRepository;
import com.springboot.webflux.products.repository.employeedb.EmployeeRepository;
import com.springboot.webflux.products.repository.productdb.CategoryRepository;
import com.springboot.webflux.products.repository.productdb.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith( SpringExtension.class )
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    classes = ProductsApplication.class
    )
@ActiveProfiles(value="test")
@AutoConfigureWebTestClient
public class IntegrationTest {
    @Value("${config.urlCategories}")
    private String CATEGORIES_URL;
    @Value("${config.urlProducts}")
    private String PRODUCTS_URL;
    
    @MockBean
	EmployeeRepository employeeRepository;
    @MockBean
    CompanyRepository companyRepository;
    @MockBean
	ProductRepository productRepository;
    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
	private WebTestClient webClient;

    private String productsBaseURL;

    private LocalDateTime now;

    @BeforeEach
    void setup(){
        this.productsBaseURL = "/"+PRODUCTS_URL+"/";

        when(productRepository.findById(any(ObjectId.class))).thenReturn(
            Mono.just(Fixtures.oneProductDAO(LocalDateTime.now()))
        );
        when(productRepository.existsById(any(ObjectId.class))).thenReturn(
            Mono.just(true)
        );

        this.now = LocalDateTime.now();
        when(productRepository.save(any())).thenReturn(
            Mono.just(Fixtures.oneProductDAO(this.now))
        );
    }

    //------------------------OK-----------------------------
    @Test
	void get_all_products_2products() {
        when(productRepository.findAll()).thenReturn(Flux.fromIterable(Fixtures.twoProductsDAO()));
        webClient.get()
        .uri(this.productsBaseURL)
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
    void get_all_categories_2categories(){
        when(categoryRepository.findAll()).thenReturn(Flux.fromIterable(Fixtures.twoCategoriesDAO()));
        webClient.get()
        .uri(this.productsBaseURL+CATEGORIES_URL)
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
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
        webClient.post()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(Fixtures.oneProduct(this.now)), ProductDTO.class)
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
        webClient.put()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(Fixtures.oneProduct(this.now)), ProductDTO.class)
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
        when(productRepository.deleteById(any(ObjectId.class))).thenReturn(
            Utils.monoVoid()
        );
        webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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

    //------------------------ERROR-----------------------------
    @Test
    void get_all_products_error(){
        when(productRepository.findAll()).thenReturn(Flux.error(new Exception("db error")));
        webClient.get()
        .uri(this.productsBaseURL)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().isEmpty());
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void get_all_categories_error(){
        when(categoryRepository.findAll()).thenReturn(Flux.error(new Exception("db error")));
        webClient.get()
        .uri(this.productsBaseURL+CATEGORIES_URL)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getCategoriesDTO().isEmpty());
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_getById_notFound(){   
        when(productRepository.findById(any(ObjectId.class))).thenReturn(
            Mono.empty()
        );     
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_getById_error(){   
        when(productRepository.findById(any(ObjectId.class))).thenReturn(
            Mono.error(new Exception("db error"))
        );     
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_insertNew_error(){
        when(productRepository.save(any())).thenReturn(
            Mono.error(new Exception("db error"))
        );
        webClient.post()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(Fixtures.oneProduct(this.now)), ProductDTO.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().isEmpty());
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_update_notFound(){        
        when(productRepository.existsById(any(ObjectId.class))).thenReturn(
            Mono.just(false)
        );
        webClient.put()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(Fixtures.oneProduct(this.now)), ProductDTO.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().isEmpty());
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_update_error(){        
        when(productRepository.existsById(any(ObjectId.class))).thenReturn(
            Mono.error(new Exception("db error"))
        );
        webClient.put()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(Fixtures.oneProduct(this.now)), ProductDTO.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().isEmpty());
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_delete_notFound(){
        when(productRepository.existsById(any(ObjectId.class))).thenReturn(
            Mono.just(false)
        );
        webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_delete_error(){
        when(productRepository.deleteById(any(ObjectId.class))).thenReturn(
            Mono.error(new Exception("db error"))
        );
        webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }
}
