package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Category;
import com.springboot.webflux.products.model.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Category to CategoryDTO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryDTOMapper {
    CategoryDTOMapper INSTANCE = Mappers.getMapper(CategoryDTOMapper.class);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Category DTOtoBO(CategoryDTO categoryDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO BOtoDTO(Category category);
}