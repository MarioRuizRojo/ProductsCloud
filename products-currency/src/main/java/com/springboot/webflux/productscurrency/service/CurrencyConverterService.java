package com.springboot.webflux.productscurrency.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springboot.webflux.productscurrency.model.bo.CurrencyTag;

import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Currency converter service
 */
@Service
public class CurrencyConverterService {
    /**
     * Update frequency of the saved exchange rate
     */        
    private Long UPDATE_FRECUENCY;//in seconds

    /**
     * Latest saved exchange rate
     */
    private Double latestSavedRate;
    /**
     * Latest time that the exchange rate was update
     */
    private Date latestExchangeRateUpdate; 
    /**
     * Id of the latest saved exchange rate 
     */
    private String rateId;

    /**
     * Coin gecko exchange rate service
     */
    private CoinGeckoService coinGeckoService;

    public CurrencyConverterService(
            CoinGeckoService coinGeckoService, 
            @Value("${exchange.update.frequency}") String S_UPDATE_FRECUENCY
            ){        
        this.UPDATE_FRECUENCY = Long.valueOf(S_UPDATE_FRECUENCY);
        this.coinGeckoService = coinGeckoService;
        this.latestSavedRate = 0.0;
        this.rateId = "";
        Instant someTimeAgo = (new Date()).toInstant().minusSeconds(UPDATE_FRECUENCY+1L);        
        this.latestExchangeRateUpdate = Date.from(someTimeAgo);
    }

    /**
     * It returns the current currency A to currency B exchange rate
     * This rate is updated if latest call was later than 
     * UPDATE_FRECUENCY seconds ago using coingecko service
     * Or if latest saved rate isn't A to B rate
     * It updates the rate calling to coin gecko service
     * @return A to B exchange rate
     */
    public Mono<Double> getAtoBrate(CurrencyTag currencyTagA,CurrencyTag currencyTagB){
        return Mono.just(Instant.now())
        .map(now->latestExchangeRateUpdate.toInstant().isBefore(now.minusSeconds(UPDATE_FRECUENCY)))
        .flatMap(rateRefreshNeeded->{
            String currentRateId = currencyTagA.getId()+"-"+currencyTagB.getId();
            if(rateRefreshNeeded || !this.rateId.equals(currentRateId)){            
                this.rateId = currentRateId;
                this.latestExchangeRateUpdate = new Date();
                return coinGeckoService.calculateExchangeRate(currencyTagA,currencyTagB);                
            }
            return Mono.just(this.latestSavedRate);
        })
        .map(rate->{
            this.latestSavedRate = rate;
            return this.latestSavedRate;
        });        
    }

    /**
     * It returns the current currency A to currency B exchange rate inverted
     * This rate is updated if latest call was later than 
     * UPDATE_FRECUENCY seconds ago using coingecko service
     * Or if latest saved rate isn't A to B rate
     * It updates the rate calling to coin gecko service
     * @return B to A exchange rate
     */
    public Mono<Double> getBtoArate(CurrencyTag currencyTagA, CurrencyTag currencyTagB){
        return this.getAtoBrate(currencyTagA, currencyTagB)
            .map(val->1.0d/val);
    }
}
