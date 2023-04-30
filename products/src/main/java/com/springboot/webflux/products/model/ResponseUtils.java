package com.springboot.webflux.products.model;

import com.springboot.webflux.products.Utils;
import com.springboot.webflux.products.model.dto.CategoryDTO;
import com.springboot.webflux.products.model.dto.ProductDTO;
import com.springboot.webflux.products.model.dto.ResponseDTO;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mario Ruiz Rojo
 * Response function tools
 */
public class ResponseUtils {

    /**
     * It runs all validations annotated in Product.Class and throws errors if validation fails
     * @param productDTO to validate
     * @return stream exception with errors if product is not valid
     */
    public static Mono<ProductDTO> validateProduct(ProductDTO productDTO, Validator validator){
        Errors errors = new BeanPropertyBindingResult(productDTO,ProductDTO.class.getName());
        validator.validate(productDTO,errors);
        if(errors.hasErrors()) {
            WebExchangeBindException myException = new WebExchangeBindException(new MethodParameter(null),new BindException(null));
            myException.addAllErrors(errors);
            throw myException;
        }
        return Mono.just(productDTO);
    }

    /**
     * It catches all errors of the current stream and sends response with bad request code
     * @param throwA is the exception with the fields error messages
     * @return API Response with bad request code and error message
     */
    public static Mono<ResponseDTO> responseErrors(Throwable throwA){
        if(throwA instanceof WebExchangeBindException){
            return Mono.just(throwA).cast(WebExchangeBindException.class)
                    .flatMap(errors -> Mono.just(errors.getFieldErrors()))
                    .flatMapMany(errors -> Flux.fromIterable(errors))
                    .map(fieldError -> "Error on Field: "+fieldError.getField()+", Message: "+fieldError.getDefaultMessage())
                    .collectList()//it puts all flux string in a single Mono list string
                    .map(msglist -> new ResponseDTO(Utils.emptyList(), Utils.emptyList(), msglist, "input param not valid"));
        }
        else{
            return Mono.just(new ResponseDTO(Utils.emptyList(), Utils.emptyList(), Utils.emptyList(), "Internal error"));
        }
    }

    /**
     * Default confirmation response ACK
     * @return ACK API Response
     */
    public static ResponseDTO emptyResponse(){
        return new ResponseDTO(Utils.emptyList(), Utils.emptyList(), Utils.emptyList(),"");
    }

    /**
     * One product response
     * @param productDTO
     * @return API Response
     */
    public static ResponseDTO fromProductDTOtoResponseDTO(ProductDTO productDTO) {
        List products = new ArrayList<>();
        products.add(productDTO);
        return fromProductDTOListToResponseDTO(products);
    }

    /**
     * Category list response
     * @param categories
     * @return API Response
     */
    public static ResponseDTO fromCategoryDTOListToResponseDTO(List<CategoryDTO> categories) {
        return new ResponseDTO(Utils.emptyList(),categories,Utils.emptyList(),"");
    }

    /**
     * Products list response
     * @param products
     * @return API Response
     */
    public static ResponseDTO fromProductDTOListToResponseDTO(List<ProductDTO> products) {
        return new ResponseDTO(products,Utils.emptyList(),Utils.emptyList(),"");
    }
}
