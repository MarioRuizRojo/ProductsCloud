package com.springboot.webflux.products.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 * @author Mario Ruiz Rojo
 * Company
 */
@Data
public class CompanyDTO {
    /**
     * Identifier of company
     */
    @NotEmpty
    private String id;

    /**
     * Name of the company
     */
    @NotEmpty
    private String name;
}