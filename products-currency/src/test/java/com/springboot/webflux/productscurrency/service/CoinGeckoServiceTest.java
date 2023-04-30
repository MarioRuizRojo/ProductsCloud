package com.springboot.webflux.productscurrency.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.springboot.webflux.productscurrency.Fixtures;
import com.springboot.webflux.productscurrency.client.CoinGeckoClient;
import com.springboot.webflux.productscurrency.model.bo.CurrencyTag;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class CoinGeckoServiceTest {    
    @MockBean
    private CoinGeckoClient coinGeckoClient;

    private CoinGeckoService coinGeckoService;

    private Mono coinGeckoError;

    private final CurrencyTag euroTag = new CurrencyTag("eur","Euro");
    private final CurrencyTag dollarTag = new CurrencyTag("usd","US Dollar");

    @BeforeEach
    void setup(){        
        this.coinGeckoService = new CoinGeckoService(
            coinGeckoClient
        );    
        this.coinGeckoError = Fixtures.monoError("coin gecko error");
    }

    @Test
    void calculateExchangeRate_ok(){
        when(coinGeckoClient.getExchangeRates()).thenReturn(
            Fixtures.coinGeckoTwoRatesMono()
        );
        StepVerifier.create(
            this.coinGeckoService.calculateExchangeRate(euroTag,dollarTag)
        )
        .consumeNextWith(actualRate->{
            Double expectedPrice = 0.5d;
            assertTrue(actualRate.equals(expectedPrice));
        })
        .expectComplete()
        .verify();
    }

    @Test
    void calculateExchangeRate_error(){
        when(coinGeckoClient.getExchangeRates()).thenReturn(coinGeckoError);
        StepVerifier.create(
            this.coinGeckoService.calculateExchangeRate(euroTag,dollarTag)
        )
        .expectError()
        .verify();
    }
}
