package com.springboot.webflux.productscurrency.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.Validator;

import com.springboot.webflux.productscurrency.Fixtures;
import com.springboot.webflux.productscurrency.client.ProductsClient;
import com.springboot.webflux.productscurrency.model.ResponseUtils;
import com.springboot.webflux.productscurrency.model.bo.CurrencyTag;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ProductServiceTest {
    @MockBean
    private PriceConverterService priceConverterService;

    @MockBean
    private ProductsClient productsClient;

    @Autowired
    Validator validator;

    private ProductService productService;

    private LocalDateTime now;

    private Mono<ResponseDTO> productClientError;
    private Mono coinGeckoError;

    @BeforeEach
    void setup(){        
        this.productService = new ProductService(productsClient,priceConverterService,validator);
        this.now = LocalDateTime.now();
        this.coinGeckoError = Fixtures.monoError("coin gecko error");
        this.productClientError = Fixtures.monoError("external service error");

    }

    //------------------------OK-----------------------------
    @Test
    void get_all_products_2products(){
        Mono<ResponseDTO> sample = Fixtures.response2productsMono();
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(sample);
        when(productsClient.getAll()).thenReturn(sample);
        StepVerifier.create(
            productService.getAll()
        )
        .consumeNextWith(r->{
            Double actualPrice = r.getProductsDTO().get(0).getPrice();
            Double expectedPrice = Fixtures.oneProduct(now).getPrice();
            assertTrue(r.getProductsDTO().size()==2);
            assertTrue(actualPrice.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_all_categories_2categories(){
        when(productsClient.getAllCategories()).thenReturn(Fixtures.response2categoriesMono());
        StepVerifier.create(
            productService.getAllCategories()
        )
        .consumeNextWith(r->{
            assertTrue(r.getCategoriesDTO().size()==2);
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_by_id_product_ok(){
        Mono<ResponseDTO> sample = Fixtures.responseOneProductMono(now);
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(sample);
        when(productsClient.getById(anyString())).thenReturn(sample);
        
        StepVerifier.create(
            productService.getById(Fixtures.oneProductId())
        )
        .consumeNextWith(r->{
            Double actualPrice = r.getProductsDTO().get(0).getPrice();
            Double expectedPrice = Fixtures.oneProduct(now).getPrice();
            assertTrue(r.getProductsDTO().size()==1);
            assertTrue(r.getErrorMsg().isEmpty());
            assertTrue(actualPrice.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void insert_new_product_ok(){
        Mono<ResponseDTO> sample = Fixtures.responseOneProductMono(now);
        when(priceConverterService.BToA(any(),any(),any())).thenReturn(Fixtures.oneProductBOMono(now));
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(sample);
        when(productsClient.insertNew(any())).thenReturn(sample);
        StepVerifier.create(
            productService.insertNew(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().size()==1);
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(r.getErrorMsg().isEmpty());
            Double actualPrice = r.getProductsDTO().get(0).getPrice();
            Double expectedPrice = Fixtures.oneProduct(now).getPrice();
            assertTrue(actualPrice.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void update_product_ok(){      
        Mono<ResponseDTO> sample = Fixtures.responseOneProductMono(now);
        when(priceConverterService.BToA(any(),any(),any())).thenReturn(Fixtures.oneProductBOMono(now));
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(sample);
        when(productsClient.update(any())).thenReturn(sample);
        StepVerifier.create(
            productService.update(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().size()==1);
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(r.getErrorMsg().isEmpty());
            Double actualPrice = r.getProductsDTO().get(0).getPrice();
            Double expectedPrice = Fixtures.oneProduct(now).getPrice();
            assertTrue(actualPrice.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void delete_product_ok(){
        when(productsClient.delete(anyString())).thenReturn(Mono.just(ResponseUtils.emptyResponse()));
        StepVerifier.create(
            productService.delete(Fixtures.oneProductId())
        )        
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    //------------------------ERROR-----------------------------
    @Test
    void get_all_products_errorProductsClient(){
        when(priceConverterService.allProductsFromAToB(any(),any(),any()))
            .thenReturn(Fixtures.response2productsMono());
        when(productsClient.getAll()).thenReturn(productClientError);
        StepVerifier.create(
            productService.getAll()
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_all_products_errorPriceConverter(){
        when(productsClient.getAll()).thenReturn(Fixtures.response2productsMono());        
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(coinGeckoError);
        StepVerifier.create(
            productService.getAll()
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_all_categories_error(){
        when(productsClient.getAllCategories()).thenReturn(productClientError);
        StepVerifier.create(
            productService.getAllCategories()
        )
        .consumeNextWith(r->{
            assertTrue(r.getCategoriesDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_by_id_product_errorProductsClient(){         
        when(priceConverterService.allProductsFromAToB(any(),any(),any()))
            .thenReturn(Fixtures.responseOneProductMono(now));       
        when(productsClient.getById(anyString())).thenReturn(productClientError);
        StepVerifier.create(
            productService.getById(Fixtures.oneProductId())
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_by_id_product_errorPriceConverter(){                
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(coinGeckoError);       
        when(productsClient.getById(anyString()))
            .thenReturn(Fixtures.responseOneProductMono(now));
        StepVerifier.create(
            productService.getById(Fixtures.oneProductId())
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void insert_new_product_errorProductsClient(){
        when(priceConverterService.BToA(any(),any(),any()))
            .thenReturn(Fixtures.oneProductBOMono(now));
        when(priceConverterService.allProductsFromAToB(any(),any(),any()))
            .thenReturn(Fixtures.responseOneProductMono(now));
        when(productsClient.insertNew(any())).thenReturn(productClientError);
        StepVerifier.create(
            productService.insertNew(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void insert_new_product_errorPriceConverter(){
        when(priceConverterService.BToA(any(),any(),any())).thenReturn(coinGeckoError);
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(coinGeckoError);
        when(productsClient.insertNew(any())).thenReturn(Fixtures.responseOneProductMono(now));
        StepVerifier.create(
            productService.insertNew(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void update_product_errorProductsClient(){      
        when(priceConverterService.BToA(any(),any(),any()))
            .thenReturn(Fixtures.oneProductBOMono(now));
        when(priceConverterService.allProductsFromAToB(any(),any(),any()))
            .thenReturn(Fixtures.responseOneProductMono(now));
        when(productsClient.update(any())).thenReturn(productClientError);
        StepVerifier.create(
            productService.update(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void update_product_errorPriceConverter(){      
        when(priceConverterService.BToA(any(),any(),any())).thenReturn(coinGeckoError);
        when(priceConverterService.allProductsFromAToB(any(),any(),any())).thenReturn(coinGeckoError);
        when(productsClient.update(any())).thenReturn(Fixtures.responseOneProductMono(now));
        StepVerifier.create(
            productService.update(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void delete_product_error(){
        when(productsClient.delete(anyString())).thenReturn(productClientError);
        StepVerifier.create(
            productService.delete(Fixtures.oneProductId())
        )        
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }
}
