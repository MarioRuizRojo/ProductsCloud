package com.springboot.webflux.productscurrency.service;

import org.springframework.stereotype.Service;

import com.springboot.webflux.productscurrency.model.ResponseUtils;
import com.springboot.webflux.productscurrency.model.bo.CurrencyTag;
import com.springboot.webflux.productscurrency.model.bo.Product;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;
import com.springboot.webflux.productscurrency.model.mapper.ProductDTOMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Product's price currency converter service
 */
@Service
public class PriceConverterService {
    /**
     * Currency converter service
     */   
    private final CurrencyConverterService currencyConverterService;

    public PriceConverterService(
            CurrencyConverterService currencyConverterService
            ){
        this.currencyConverterService = currencyConverterService;
    }

    /**
	 * It converts product's price from currency A to currency B
	 * It may call to coin gecko REST api to get the current exchange rate
	 * @param product with price in A currency
	 * @return product with price in B currency
	 */
	public Mono<Product> AToB(
        Product product, 
        CurrencyTag currencyTagA, 
        CurrencyTag currencyTagB
        ){		
            return currencyConverterService.getAtoBrate(currencyTagA,currencyTagB)
            .map(rate->{
                product.setPrice(product.getPrice()*rate);
                return product;
            });
	}

    /**
	 * It converts product's price from currency B to currency A
	 * It may call to coin gecko REST api to get the current exchange rate
	 * @param product with price in B currency
	 * @return product with price in A currency
	 */
	public Mono<Product> BToA(
        Product product, 
        CurrencyTag currencyTagA, 
        CurrencyTag currencyTagB
        ){				
            return currencyConverterService.getBtoArate(currencyTagA, currencyTagB)
            .map(rate->{
                product.setPrice(product.getPrice()*rate);
                return product;
            });
	}

    /**
     * Convert all product prices from currency A to currency B
     * add them to API response
     * @param clientResponse response received from products service
     * @return API response
     */
    public Mono<ResponseDTO> allProductsFromAToB(
        ResponseDTO clientResponse,
        CurrencyTag currencyTagA, 
        CurrencyTag currencyTagB
        ) {
            return Mono.just(clientResponse.getProductsDTO())
                .flatMapMany(receivedProducts->Flux.fromIterable(receivedProducts))
                .map(ProductDTOMapper.INSTANCE::DTOtoBO)
                .flatMap(product->this.AToB(product,currencyTagA,currencyTagB))
                .map(ProductDTOMapper.INSTANCE::BOtoDTO)
                .collectList()
                .map(ResponseUtils::fromProductDTOListToResponseDTO)
                .onErrorResume(ResponseUtils::responseErrors);
    }
}
