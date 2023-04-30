package com.springboot.webflux.products.model;

import com.springboot.webflux.products.model.dao.productdb.ProductDAO;
import com.springboot.webflux.products.model.dto.ProductDTO;
import com.springboot.webflux.products.model.mapper.ProductDAOMapper;
import com.springboot.webflux.products.model.mapper.ProductDTOMapper;
import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Product function tools
 */
public class ProductUtils {
    /**
     * Double convertion, from ProductDAO to ProductDTO
     * @param ProductDAO
     * @return ProductDTO
     */
    public static Mono<ProductDTO> fromDAOtoDTO(ProductDAO input){
        return Mono.just(ProductDAOMapper.INSTANCE.DAOtoBO(input))
                .map(ProductDTOMapper.INSTANCE::BOtoDTO);
    }

    /**
     * Double convertion, from ProductDTO to ProductDAO
     * @param ProductDTO
     * @return ProductDAO
     */
    public static ProductDAO fromDTOtoDAO(ProductDTO input){
        return ProductDAOMapper.INSTANCE.BOtoDAO(
            ProductDTOMapper.INSTANCE.DTOtoBO(input)
        );
    }
}
