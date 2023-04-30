package com.springboot.webflux.productscurrency.model.bo;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 *
 * @author Mario Ruiz Rojo
 * CurrencyTag
 * Currency filter object for coin gecko service
 */
@Data
@AllArgsConstructor
public class CurrencyTag {
    /**
     * Identifier
     */
    private String id;
    /**
     * Currency's displayed name
     */
    private String name;    
}
