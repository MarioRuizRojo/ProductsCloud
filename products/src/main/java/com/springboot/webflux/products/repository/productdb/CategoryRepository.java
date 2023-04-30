package com.springboot.webflux.products.repository.productdb;

import com.springboot.webflux.products.model.dao.productdb.CategoryDAO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 *
 * @author Mario Ruiz Rojo
 * Collection query executer for Category documents
 */
public interface CategoryRepository extends ReactiveMongoRepository<CategoryDAO, ObjectId> {
}
