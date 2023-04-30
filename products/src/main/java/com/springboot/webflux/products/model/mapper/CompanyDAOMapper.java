package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Company;
import com.springboot.webflux.products.model.dao.employeedb.CompanyDAO;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Company to CompanyDAO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = ObjectId.class)
public interface CompanyDAOMapper {
    CompanyDAOMapper INSTANCE = Mappers.getMapper(CompanyDAOMapper.class);
    @Mapping(target = "id", expression = "java(companyDAO.getId().toString())")
    @Mapping(target = "name", source = "name")
    Company DAOtoBO(CompanyDAO companyDAO);

    @Mapping(target = "id", expression = "java(new ObjectId(company.getId()))")
    @Mapping(target = "name", source = "name")
    CompanyDAO BOtoDAO(Company company);
}