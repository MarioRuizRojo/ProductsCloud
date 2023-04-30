package com.springboot.webflux.productscurrency.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springboot.webflux.productscurrency.client.CoinGeckoClient;
import com.springboot.webflux.productscurrency.model.bo.CurrencyTag;

import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Coin gecko exchange rate service
 */
@Service
public class CoinGeckoService {
    /**
     * Web Client for coingecko web service
     */   
    private final CoinGeckoClient coinGeckoClient;

    public CoinGeckoService(
            CoinGeckoClient coinGeckoClient
            ){        
        this.coinGeckoClient = coinGeckoClient;
    }

    /**
     * It extracts currency A to currency B exchange rate from coingecko response
     * Reads rates of A and B from coingecko response 
     * and calculates A to B rate
     * @return A to B rate
     */
    public Mono<Double> calculateExchangeRate(CurrencyTag currencyTagA, CurrencyTag currencyTagB){
        return this.coinGeckoClient.getExchangeRates()
            .map( coingecko -> Arrays.asList(
                coingecko.getRates().get(currencyTagA.getId()), 
                coingecko.getRates().get(currencyTagB.getId())
                ) 
            )
            .map(twoRates -> {
                Optional<Double> eurRateO = twoRates.stream()
                    .filter(rate->rate.getName().equals(currencyTagA.getName()))
                    .map(rate->rate.getValue())
                    .findFirst();
                Optional<Double> usdRateO = twoRates.stream()
                    .filter(rate->rate.getName().equals(currencyTagB.getName()))
                    .map(rate->rate.getValue())
                    .findFirst();
                if(eurRateO.isPresent() && usdRateO.isPresent()){
                    return usdRateO.get()/eurRateO.get();
                }
                return 0.0;
            });
    }
}
