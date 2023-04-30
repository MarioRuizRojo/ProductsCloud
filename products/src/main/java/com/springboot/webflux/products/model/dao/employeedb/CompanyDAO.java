package com.springboot.webflux.products.model.dao.employeedb;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Mario Ruiz Rojo
 * It represents companies collection
 */
@Data
@Document(collection="companies")
public class CompanyDAO {
    /**
     * Identifier of company
     */
    @NotNull
    @Id
    private ObjectId id;

    /**
     * Name of the company
     */
    @NotEmpty
    private String name;
}
