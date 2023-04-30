package com.springboot.webflux.productscurrency.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author Mario Ruiz Rojo
 * API Response with request field errors, product response list, 
 * category response list or api error message
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDTO {
    private List<ProductDTO> productsDTO;
    private List<CategoryDTO> categoriesDTO;
    private List<String> fieldErrors;
    private String errorMsg;
}
