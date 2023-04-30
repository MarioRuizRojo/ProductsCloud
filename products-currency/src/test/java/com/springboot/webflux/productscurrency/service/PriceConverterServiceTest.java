package com.springboot.webflux.productscurrency.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.springboot.webflux.productscurrency.Fixtures;
import com.springboot.webflux.productscurrency.model.bo.CurrencyTag;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class PriceConverterServiceTest {
    @Value("${exchange.update.frequency}") 
    private String S_UPDATE_FRECUENCY;
    
    @MockBean
    private CurrencyConverterService currencyConverterService;

    private PriceConverterService priceConverterService;

    private LocalDateTime now;

    private Mono coinGeckoError;

    private final Double EUR_TO_USD_RATE = 0.5d;
    private final Double USD_TO_EUR_RATE = 2.0d;
    private final CurrencyTag euroTag = new CurrencyTag("eur","Euro");
    private final CurrencyTag dollarTag = new CurrencyTag("usd","US Dollar");

    @BeforeEach
    void setup(){  
        when(currencyConverterService.getAtoBrate(any(), any())).thenReturn(
            Mono.just(EUR_TO_USD_RATE)
        );
        when(currencyConverterService.getBtoArate(any(), any())).thenReturn(
            Mono.just(USD_TO_EUR_RATE)
        );
        this.now = LocalDateTime.now();
        this.priceConverterService = new PriceConverterService(
            currencyConverterService
        );    
        this.coinGeckoError = Fixtures.monoError("coin gecko error");
    }

    @Test
    void AToB_ok(){
        StepVerifier.create(
            this.priceConverterService.AToB(
                Fixtures.oneProductBO(this.now),
                euroTag,
                dollarTag
            )
        )
        .consumeNextWith(r->{
            Double actualPrice = r.getPrice();
            Double expectedPrice = Fixtures.oneProduct(now).getPrice()*EUR_TO_USD_RATE;
            assertTrue(actualPrice.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void BToA_ok(){
        StepVerifier.create(
            this.priceConverterService.BToA(
                Fixtures.oneProductBO(this.now),
                euroTag,
                dollarTag
            )
        )
        .consumeNextWith(r->{
            Double actualPrice = r.getPrice();
            Double expectedPrice = Fixtures.oneProduct(now).getPrice()*USD_TO_EUR_RATE;
            assertTrue(actualPrice.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void allProductsFromAToB_ok(){
        StepVerifier.create(
            this.priceConverterService.allProductsFromAToB(
                Fixtures.response2products(),
                euroTag,
                dollarTag
            )
        )
        .consumeNextWith(r->{
            Double actualPrice = r.getProductsDTO().get(0).getPrice();
            Double expectedPrice = Fixtures.oneProduct(now).getPrice()*EUR_TO_USD_RATE;
            assertTrue(actualPrice.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void AToB_error(){
        when(currencyConverterService.getAtoBrate(any(), any())).thenReturn(coinGeckoError);
        StepVerifier.create(
            this.priceConverterService.AToB(
                Fixtures.oneProductBO(this.now),
                euroTag,
                dollarTag
            )
        )
        .expectError()
        .verify();
    }

    @Test
    void BToA_error(){
        when(currencyConverterService.getBtoArate(any(),any())).thenReturn(coinGeckoError);
        StepVerifier.create(
            this.priceConverterService.BToA(
                Fixtures.oneProductBO(this.now),
                euroTag,
                dollarTag
            )
        )
        .expectError()
        .verify();
    }
    
    @Test
    void allProductsFromAToB_error(){
        when(currencyConverterService.getAtoBrate(any(),any())).thenReturn(coinGeckoError);
        StepVerifier.create(
            this.priceConverterService.allProductsFromAToB(
                Fixtures.response2products(),
                euroTag,
                dollarTag
            )
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }
}
