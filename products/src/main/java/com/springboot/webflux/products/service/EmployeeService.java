package com.springboot.webflux.products.service;

import com.springboot.webflux.products.model.EmployeeUtils;
import com.springboot.webflux.products.model.dto.EmployeeDTO;
import com.springboot.webflux.products.repository.employeedb.EmployeeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 *
 * @author Mario Ruiz Rojo
 * Sample Employee service to test the second DB connexion
 */
@Service
public class EmployeeService {
    /**
     * Employee collection repository
     */
    private final EmployeeRepository employeeRepository;
    public EmployeeService(
            EmployeeRepository employeeRepository
    ){
        this.employeeRepository = employeeRepository;
    }
    /**
     * it returns the list of employees
     * @return list of employees
     */
    public Flux<EmployeeDTO> getAll() {
        return employeeRepository.findAll()
                .flatMap(EmployeeUtils::fromDAOtoDTO);
    }
}
