package com.springboot.webflux.productscurrency.service;

import com.springboot.webflux.productscurrency.client.ProductsClient;
import com.springboot.webflux.productscurrency.model.ResponseUtils;
import com.springboot.webflux.productscurrency.model.bo.CurrencyTag;
import com.springboot.webflux.productscurrency.model.dto.ProductDTO;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;
import com.springboot.webflux.productscurrency.model.mapper.ProductDTOMapper;

import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * All product CRUD API endpoints service implementation with currency exchange calculation for prices
 */
@Service
public class ProductService {
    /**
     * Web Client for products web service
     */
    private final ProductsClient productsClient;

    /**
     * Product's price currency converter service
     */
    private final PriceConverterService priceConverterService;
    /**
     * Validator of ProductDTO
     */
    private final Validator validator;

    /**
     * Euro filter
     */
    private final CurrencyTag euroTag = new CurrencyTag("eur","Euro");

    /**
     * Dollar filter
     */
    private final CurrencyTag dollarTag = new CurrencyTag("usd","US Dollar");

    public ProductService(
            ProductsClient productsClient,
            PriceConverterService priceConverterService,
            Validator validator
    ){        
        this.productsClient = productsClient;
        this.validator = validator;        
        this.priceConverterService = priceConverterService;
    }

    //----------PRIVATE----------

    /**
     * It runs validation on ProductDTO
     * and converts its price to euros
     * @param productReceived
     * @return ProductDTO validated and its price on euros
     */
    private Mono<ProductDTO> validateProductReceivedConvertToEuros(ProductDTO productReceived){
        return ResponseUtils.validateProduct(productReceived,validator)
                .map(ProductDTOMapper.INSTANCE::DTOtoBO)
                .flatMap(product->priceConverterService.BToA(
                    product,
                    euroTag,
                    dollarTag
                ))
                .map(ProductDTOMapper.INSTANCE::BOtoDTO);
    }

    /**
     * It returns Response with error message if received response 
     * has error message or empty product list
     * Else it returns products with converted prices into dollars
     * @param clientResponse
     * @return
     */
    private Mono<ResponseDTO> returnErrorOrConvertToDollars(ResponseDTO clientResponse) {
        return Mono.just(clientResponse)
        .flatMap(clientResponse1->{
            if(!clientResponse1.getErrorMsg().isEmpty()){
                return Mono.just(clientResponse1);
            }
            if(clientResponse1.getProductsDTO().isEmpty()){
                ResponseDTO errorResponse = ResponseUtils.emptyResponse();
                errorResponse.setErrorMsg("Internal Error");
                return Mono.just(errorResponse);
            }
            return priceConverterService.allProductsFromAToB(
                clientResponse1,
                euroTag,
                dollarTag
            );
        });
    }

    //----------PUBLIC----------

    /**
     * Fetch all Products from products web service
     * convert all of their prices to dollars
     * @return API Response
     */
    public Mono<ResponseDTO> getAll() {
        return productsClient.getAll()
            .flatMap(this::returnErrorOrConvertToDollars)
            .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * Fetch all Categories from products web service
     * @return API Response
     */
    public Mono<ResponseDTO> getAllCategories() {
        return productsClient.getAllCategories()
            .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * Search one Product by id in products service
     * converts its price into dollars
     * @return API Response
     */
    public Mono<ResponseDTO> getById(String id) {
        return productsClient.getById(id)
            .flatMap(this::returnErrorOrConvertToDollars)
            .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * It validates one Product
     * converts its price into euros then inserts it
     * It converts the resultant product's price into dollars
     * @return API Response
     */
    public Mono<ResponseDTO> insertNew(ProductDTO productReceived) {
        return validateProductReceivedConvertToEuros(productReceived)
            .flatMap(productsClient::insertNew)
            .flatMap(this::returnErrorOrConvertToDollars)
            .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * It validates one Product
     * converts its price into euros then updates it
     * It converts the resultant product's price into dollars
     * @return API Response
     */
    public Mono<ResponseDTO> update(ProductDTO productReceived) {
        return validateProductReceivedConvertToEuros(productReceived)            
            .flatMap(productsClient::update)
            .flatMap(this::returnErrorOrConvertToDollars)
            .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * It deletes one product by id
     * and sends back an ACK as API Response 
     * @return API Response with error msg in case it doesn't exist
     */
    public Mono<ResponseDTO> delete(String id) {
        return productsClient.delete(id)
        .onErrorResume(ResponseUtils::responseErrors);
    }
}
