package com.springboot.webflux.products.model.mapper;

import com.springboot.webflux.products.model.bo.Employee;
import com.springboot.webflux.products.model.dao.employeedb.EmployeeDAO;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Mario Ruiz Rojo
 * Converter from Employee to EmployeeDAO and reverse
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = ObjectId.class)
public interface EmployeeDAOMapper {
    EmployeeDAOMapper INSTANCE = Mappers.getMapper(EmployeeDAOMapper.class);
    @Mapping(target = "id", expression = "java(employeeDAO.getId().toString())")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "company", expression = "java(CompanyDAOMapper.INSTANCE.DAOtoBO(employeeDAO.getCompany()))")
    Employee DAOtoBO(EmployeeDAO employeeDAO);

    @Mapping(target = "id", expression = "java(new ObjectId(employee.getId()))")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "company", expression = "java(CompanyDAOMapper.INSTANCE.BOtoDAO(employee.getCompany()))")
    EmployeeDAO BOtoDAO(Employee employee);


}