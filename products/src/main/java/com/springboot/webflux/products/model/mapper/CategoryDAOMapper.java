package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Category;
import com.springboot.webflux.products.model.dao.productdb.CategoryDAO;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Category to CategoryDAO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = ObjectId.class)
public interface CategoryDAOMapper {
    CategoryDAOMapper INSTANCE = Mappers.getMapper(CategoryDAOMapper.class);
    @Mapping(target = "id", expression = "java(categoryDAO.getId().toString())")
    @Mapping(target = "name", source = "name")
    Category DAOtoBO(CategoryDAO categoryDAO);

    @Mapping(target = "id", expression = "java(new ObjectId(category.getId()))")
    @Mapping(target = "name", source = "name")
    CategoryDAO BOtoDAO(Category category);
}