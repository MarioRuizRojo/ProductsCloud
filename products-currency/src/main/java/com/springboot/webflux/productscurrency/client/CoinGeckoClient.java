package com.springboot.webflux.productscurrency.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.webflux.productscurrency.model.dto.CoinGeckoDTO;

import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Coin Gecko web client
 * It requests exchange rate list from coingecko service
 */
@Service
public class CoinGeckoClient {    

    /**
     * Web Client to make requests to coingecko
     */
    private WebClient webClient;

    /**
     *
     * Setup URL of external service coingecko
     * @param urlCoinGecko
     */
    public CoinGeckoClient(@Value("${external.coingecko.url}") String urlCoinGecko){
        webClient = WebClient.builder().baseUrl(urlCoinGecko).build();
    }

    /**
	 * GET exchange rate list from coingecko
	 */
    public Mono<CoinGeckoDTO> getExchangeRates(){
		return webClient.get()
		.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		.bodyToMono(CoinGeckoDTO.class);
    }
}
