package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Employee;
import com.springboot.webflux.products.model.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Employee to EmployeeDTO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeDTOMapper {
    EmployeeDTOMapper INSTANCE = Mappers.getMapper(EmployeeDTOMapper.class);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "company", source = "companyDTO")
    Employee DTOtoBO(EmployeeDTO employeeDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "companyDTO", source = "company")
    EmployeeDTO BOtoDTO(Employee employee);
}