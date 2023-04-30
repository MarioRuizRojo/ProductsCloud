package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Product;
import com.springboot.webflux.products.model.dao.productdb.ProductDAO;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Product to ProductDAO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = ObjectId.class)
public interface ProductDAOMapper {
    ProductDAOMapper INSTANCE = Mappers.getMapper(ProductDAOMapper.class);
    @Mapping(target = "id", expression = "java(productDAO.getId().toString())")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "category", expression = "java(CategoryDAOMapper.INSTANCE.DAOtoBO(productDAO.getCategory()))")
    @Mapping(target = "picture", source = "picture")
    Product DAOtoBO(ProductDAO productDAO);

    @Mapping(target = "id", expression = "java(new ObjectId(product.getId()))")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "category", expression = "java(CategoryDAOMapper.INSTANCE.BOtoDAO(product.getCategory()))")
    @Mapping(target = "picture", source = "picture")
    ProductDAO BOtoDAO(Product product);


}