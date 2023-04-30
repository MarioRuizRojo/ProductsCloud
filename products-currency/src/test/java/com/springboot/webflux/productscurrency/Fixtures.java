package com.springboot.webflux.productscurrency;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.webflux.productscurrency.model.bo.Product;
import com.springboot.webflux.productscurrency.model.dto.CategoryDTO;
import com.springboot.webflux.productscurrency.model.dto.CoinGeckoDTO;
import com.springboot.webflux.productscurrency.model.dto.ProductDTO;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;
import com.springboot.webflux.productscurrency.model.dto.coingecko.RateDTO;
import com.springboot.webflux.productscurrency.model.mapper.ProductDTOMapper;

import reactor.core.publisher.Mono;

/**
 * Fixed values and objects for testing
 */
public class Fixtures {
    public static CategoryDTO oneCategory(){
        return new CategoryDTO("64410801ce3862e9c283e98d","Electronics");
    }
    public static List<CategoryDTO> twoCategories(){
        List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
        categories.add(oneCategory());
        categories.add(new CategoryDTO("64410801ce3862e9c283e98a","Sports"));
        return categories;
    }

    public static ProductDTO oneProduct(LocalDateTime now){
        return new ProductDTO("64410801ce3862e9c283e98e","mouse",
            1.2,now,oneCategory(),"");
    }

    public static Mono<ProductDTO> oneProductMono(LocalDateTime now){
        return Mono.just(oneProduct(now));
    }

    public static Product oneProductBO(LocalDateTime now){
        return ProductDTOMapper.INSTANCE.DTOtoBO(Fixtures.oneProduct(now));
    }

    public static Mono<Product> oneProductBOMono(LocalDateTime now){
        return Mono.just(Fixtures.oneProductBO(now));
    }

    public static List<ProductDTO> oneProductList(LocalDateTime now){      
        List<ProductDTO> products = new ArrayList<ProductDTO>();
        products.add(oneProduct(now));
        return products;
    }

    public static List<ProductDTO> twoProducts(){        
        LocalDateTime now = LocalDateTime.now();        
        ProductDTO p2 = new ProductDTO("64410801ce3862e9c283e990","keyboard",
            3.1,now,oneCategory(),"");
        List<ProductDTO> products = new ArrayList<ProductDTO>();
        products.add(oneProduct(now));
        products.add(p2);
        return products;
    }

    public static ResponseDTO response2products(){
        return new ResponseDTO(twoProducts(), Utils.emptyList(), 
            Utils.emptyList(), "");
    }

    public static Mono<ResponseDTO> response2productsMono(){
        return Mono.just(Fixtures.response2products());
    }

    public static ResponseDTO response2categories(){
        return new ResponseDTO(Utils.emptyList(), twoCategories(), 
            Utils.emptyList(), "");
    }

    public static Mono<ResponseDTO> response2categoriesMono(){
        return Mono.just(Fixtures.response2categories());
    }

    public static String oneProductId(){
        return "64410801ce3862e9c283e98e";
    }
    public static ResponseDTO responseOneProduct(LocalDateTime now) {
        return new ResponseDTO(oneProductList(now), Utils.emptyList(), 
            Utils.emptyList(), "");
    }

    public static Mono<ResponseDTO> responseOneProductMono(LocalDateTime now) {
        return Mono.just(Fixtures.responseOneProduct(now));
    }
    public static Mono monoError(String errorMsg) {
        return Mono.error(new Exception(errorMsg));
    }
    public static CoinGeckoDTO coinGeckoTwoRates() {
        RateDTO rateUS = new RateDTO("US Dollar", "$", 1.0, "fiat");
        RateDTO rateEU = new RateDTO("Euro", "€", 2.0, "fiat");
        Map<String,RateDTO> rates = new HashMap<String,RateDTO>();
        rates.put("usd", rateUS);
        rates.put("eur", rateEU);
        return new CoinGeckoDTO(rates);
    }
    public static Mono<CoinGeckoDTO> coinGeckoTwoRatesMono() {
        return Mono.just(Fixtures.coinGeckoTwoRates());
    }
}
