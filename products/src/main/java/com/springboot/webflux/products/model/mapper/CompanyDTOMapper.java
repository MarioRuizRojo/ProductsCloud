package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Company;
import com.springboot.webflux.products.model.dto.CompanyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Company to CompanyDTO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyDTOMapper {
    CompanyDTOMapper INSTANCE = Mappers.getMapper(CompanyDTOMapper.class);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Company DTOtoBO(CompanyDTO companyDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CompanyDTO BOtoDTO(Company company);
}