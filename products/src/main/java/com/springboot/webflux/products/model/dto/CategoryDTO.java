package com.springboot.webflux.products.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Mario Ruiz Rojo
 * Category
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    /**
     * Identifier of category
     */
    @NotEmpty
    private String id;

    /**
     * Name of category
     */
    @NotEmpty
    private String name;
}
