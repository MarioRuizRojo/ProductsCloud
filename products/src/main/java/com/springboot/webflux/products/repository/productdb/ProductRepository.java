package com.springboot.webflux.products.repository.productdb;

import com.springboot.webflux.products.model.dao.productdb.ProductDAO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 *
 * @author Mario Ruiz Rojo
 * Collection query executer for Product documents
 */
public interface ProductRepository extends ReactiveMongoRepository<ProductDAO, ObjectId> {
}