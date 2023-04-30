package com.springboot.webflux.productscurrency.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
public class CurrencyConverterServiceTest {
    @Value("${exchange.update.frequency}") 
    private String S_UPDATE_FRECUENCY;
    
    @MockBean
    private CoinGeckoService coinGeckoService;

    private CurrencyConverterService currencyConverterService;

    private Mono coinGeckoError;

    private final CurrencyTag euroTag = new CurrencyTag("eur","Euro");
    private final CurrencyTag dollarTag = new CurrencyTag("usd","US Dollar");

    @BeforeEach
    void setup(){  
        when(coinGeckoService.calculateExchangeRate(any(),any())).thenReturn(
            Mono.just(0.5d)
        );
        this.currencyConverterService = new CurrencyConverterService(
            coinGeckoService, 
            this.S_UPDATE_FRECUENCY
        );    
        this.coinGeckoError = Fixtures.monoError("coin gecko error");
    }

    @Test
    void getAtoBrate_ok(){
        StepVerifier.create(
            this.currencyConverterService.getAtoBrate(euroTag, dollarTag)
        )
        .consumeNextWith(actualRate->{
            Double expectedRate = 0.5d;
            assertTrue(actualRate.equals(expectedRate));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void getBtoArate_ok(){
        StepVerifier.create(
            this.currencyConverterService.getBtoArate(euroTag, dollarTag)
        )
        .consumeNextWith(actualRate->{
            Double expectedRate = 2.0d;
            assertTrue(actualRate.equals(expectedRate));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void getAtoBrate_error(){
        when(coinGeckoService.calculateExchangeRate(any(),any())).thenReturn(coinGeckoError);
        StepVerifier.create(
            this.currencyConverterService.getAtoBrate(euroTag, dollarTag)
        )
        .expectError()
        .verify();
    }

    @Test
    void getBtoArate_error(){
        when(coinGeckoService.calculateExchangeRate(any(),any())).thenReturn(coinGeckoError);
        StepVerifier.create(
            this.currencyConverterService.getBtoArate(euroTag, dollarTag)
        )
        .expectError()
        .verify();
    }
}
