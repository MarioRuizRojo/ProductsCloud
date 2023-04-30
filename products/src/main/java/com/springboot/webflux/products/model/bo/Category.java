package com.springboot.webflux.products.model.bo;

import lombok.Data;

/**
 *
 * @author Mario Ruiz Rojo
 * Category
 */
@Data
public class Category {
    /**
     * Identifier of category
     */
    private String id;

    /**
     * Name of category
     */
    private String name;
}
