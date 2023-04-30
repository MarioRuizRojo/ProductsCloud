package com.springboot.webflux.productscurrency.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 *
 * @author Mario Ruiz Rojo
 * Product
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    /**
     * Identifier of product
     */
    @NotEmpty
    private String id;

    /**
     * Name of product
     */
    @NotEmpty
    private String name;

    /**
     * Price of product
     */
    @NotNull
    private Double price;

    /**
     * Creation date of product
     */
    @DateTimeFormat(pattern="YYYY-MM-DD[T]HH:mm:ss.SSS[Z]")
    @NotNull
    private LocalDateTime createdAt;

    /**
     * Category of product
     */
    @Valid
    @NotNull
    private CategoryDTO categoryDTO;

    /**
     * Picture file name of product
     */
    private String picture;
}
