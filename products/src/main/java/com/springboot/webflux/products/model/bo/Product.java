package com.springboot.webflux.products.model.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author Mario Ruiz Rojo
 * Product
 */
@Data
public class Product {
    /**
     * Identifier of product
     */
    private String id;

    /**
     * Name of product
     */
    private String name;

    /**
     * Price of product
     */
    private Double price;

    /**
     * Creation date of product
     */
    private LocalDateTime createdAt;

    /**
     * Category of product
     */
    private Category category;

    /**
     * Picture file name of product
     */
    private String picture;
}
