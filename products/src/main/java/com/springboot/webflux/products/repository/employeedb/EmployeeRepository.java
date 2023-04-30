package com.springboot.webflux.products.repository.employeedb;

import com.springboot.webflux.products.model.dao.employeedb.EmployeeDAO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 *
 * @author Mario Ruiz Rojo
 * Collection query executer for Employee documents
 */
public interface EmployeeRepository extends ReactiveMongoRepository<EmployeeDAO,String> {
}
