package com.springboot.webflux.products.repository.employeedb;

import com.springboot.webflux.products.model.dao.employeedb.CompanyDAO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 *
 * @author Mario Ruiz Rojo
 * Collection query executer for Company documents
 */
public interface CompanyRepository extends ReactiveMongoRepository<CompanyDAO,String> {
}
