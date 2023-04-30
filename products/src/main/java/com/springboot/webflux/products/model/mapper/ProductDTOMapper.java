package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Product;
import com.springboot.webflux.products.model.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Product to ProductDTO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDTOMapper {
    ProductDTOMapper INSTANCE = Mappers.getMapper(ProductDTOMapper.class);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "category", source = "categoryDTO")
    @Mapping(target = "picture", source = "picture")
    Product DTOtoBO(ProductDTO productDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "picture", source = "picture")
    ProductDTO BOtoDTO(Product product);
}