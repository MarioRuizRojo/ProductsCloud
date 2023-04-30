package com.springboot.webflux.productscurrency;

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
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.webflux.productscurrency.client.CoinGeckoClient;
import com.springboot.webflux.productscurrency.client.ProductsClient;
import com.springboot.webflux.productscurrency.model.ResponseUtils;
import com.springboot.webflux.productscurrency.model.dto.ProductDTO;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;

import reactor.core.publisher.Mono;

@ExtendWith( SpringExtension.class )
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    classes = ProductscurrencyApplication.class
    )
@ActiveProfiles(value="test")
@AutoConfigureWebTestClient
public class IntegrationTest {
    @Value("${config.urlCategories}")
    private String CATEGORIES_URL;
    @Value("${config.urlProducts}")
    private String PRODUCTS_URL;
    
    @MockBean
	CoinGeckoClient coinGeckoClient;
    @MockBean
    ProductsClient productsClient;

    @Autowired
	private WebTestClient webClient;

    private String productsBaseURL;

    private LocalDateTime now;

    private Mono<ResponseDTO> productsClientError;
    private Mono geckoClientError;

    private final Double expectePrice = 0.6d;

    @BeforeEach
    void setup(){
        this.productsBaseURL = "/"+PRODUCTS_URL+"/";
        this.now = LocalDateTime.now();

        when(coinGeckoClient.getExchangeRates()).thenReturn(
            Fixtures.coinGeckoTwoRatesMono()
        );

        this.geckoClientError = Fixtures.monoError("coin gecko error");
        this.productsClientError = Fixtures.monoError("external service error");
    }

    //------------------------OK-----------------------------
    @Test
	void get_all_products_2products() {
        when(productsClient.getAll()).thenReturn(
            Fixtures.response2productsMono()
        );
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
            assertTrue(responseDTO.getProductsDTO().get(0).getPrice().equals(expectePrice));
        });
    }

    @Test
    void get_all_categories_2categories(){
        when(productsClient.getAllCategories()).thenReturn(
            Fixtures.response2categoriesMono()
        );
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
        when(productsClient.getById(anyString())).thenReturn(
            Fixtures.responseOneProductMono(now)
        ); 
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(responseDTO.getProductsDTO().get(0).getPrice().equals(expectePrice));
        });
    }

    @Test
    void product_insertNew_ok(){
        when(productsClient.insertNew(any())).thenReturn(
            Fixtures.responseOneProductMono(now)
        ); 
        webClient.post()
        .uri(productsBaseURL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Fixtures.oneProductMono(this.now), ProductDTO.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ResponseDTO.class)
        .consumeWith(resBody -> {
            ResponseDTO responseDTO = resBody.getResponseBody();
            assertTrue(responseDTO!=null);
            assertTrue(responseDTO.getProductsDTO().size()==1);
            assertTrue(responseDTO.getProductsDTO().get(0).getPrice().equals(expectePrice));
        });
    }

    @Test
    void product_update_ok(){       
        when(productsClient.update(any())).thenReturn(
            Fixtures.responseOneProductMono(now)
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
            assertTrue(responseDTO.getProductsDTO().size()==1);
            assertTrue(responseDTO.getProductsDTO().get(0).getPrice().equals(expectePrice));
        });
    }

    @Test
    void product_delete_ok(){
        when(productsClient.delete(anyString())).thenReturn(
            Mono.just(ResponseUtils.emptyResponse())
        ); 
        webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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

    //------------------------ERROR-----------------------------
    @Test
    void get_all_products_productsError(){
        when(productsClient.getAll()).thenReturn(this.productsClientError);
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
    void get_all_products_geckoError(){
        when(productsClient.getAll()).thenReturn(
            Fixtures.response2productsMono()
        );
        when(coinGeckoClient.getExchangeRates()).thenReturn(
            geckoClientError
        );
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
        when(productsClient.getAllCategories()).thenReturn(this.productsClientError);
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
    void product_getById_productsError(){   
        when(productsClient.getById(anyString())).thenReturn(this.productsClientError);
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_getById_geckoError(){   
        when(productsClient.getById(anyString())).thenReturn(
            Fixtures.responseOneProductMono(now)
        );
        when(coinGeckoClient.getExchangeRates()).thenReturn(
            geckoClientError
        );
        webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }

    @Test
    void product_insertNew_productsError(){
        when(productsClient.insertNew(any())).thenReturn(this.productsClientError);
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
    void product_insertNew_geckoError(){
        when(productsClient.insertNew(any())).thenReturn(
            Fixtures.responseOneProductMono(now)
        );
        when(coinGeckoClient.getExchangeRates()).thenReturn(
            geckoClientError
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
    void product_update_productsError(){        
        when(productsClient.update(any())).thenReturn(this.productsClientError);
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
    void product_update_geckoError(){        
        when(productsClient.update(any())).thenReturn(
            Fixtures.responseOneProductMono(now)
        );
        when(coinGeckoClient.getExchangeRates()).thenReturn(
            geckoClientError
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
    void product_delete_error(){
        when(productsClient.delete(anyString())).thenReturn(this.productsClientError);
        webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path(this.productsBaseURL+"{id}")
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
            assertTrue(!responseDTO.getErrorMsg().isEmpty());
        });
    }
}
