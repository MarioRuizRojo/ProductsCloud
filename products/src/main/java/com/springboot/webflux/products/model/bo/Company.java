package com.springboot.webflux.products.model.bo;

import lombok.Data;

/**
 *
 * @author Mario Ruiz Rojo
 * Company
 */
@Data
public class Company {
    /**
     * Identifier of company
     */
    private String id;

    /**
     * Name of the company
     */
    private String name;
}
