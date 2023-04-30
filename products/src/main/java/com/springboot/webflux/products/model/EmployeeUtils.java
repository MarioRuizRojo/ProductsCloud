package com.springboot.webflux.products.model;

import com.springboot.webflux.products.model.dao.employeedb.EmployeeDAO;
import com.springboot.webflux.products.model.dto.EmployeeDTO;
import com.springboot.webflux.products.model.mapper.EmployeeDAOMapper;
import com.springboot.webflux.products.model.mapper.EmployeeDTOMapper;
import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Employee function tools
 */
public class EmployeeUtils {
    /**
     * Double convertion, from EmployeeDAO to EmployeeDTO
     * @param EmployeeDAO
     * @return EmployeeDTO
     */
    public static Mono<EmployeeDTO> fromDAOtoDTO(EmployeeDAO input){
        return Mono.just(EmployeeDAOMapper.INSTANCE.DAOtoBO(input))
                .map(EmployeeDTOMapper.INSTANCE::BOtoDTO);
    }
}
