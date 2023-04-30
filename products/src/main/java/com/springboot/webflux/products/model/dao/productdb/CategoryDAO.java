package com.springboot.webflux.products.model.dao.productdb;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Mario Ruiz Rojo
 * It represents categories collection
 */
@Data
@Document(collection="categories")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDAO {
    /**
     * Identifier of category
     */
    @NotNull
    @Id
    private ObjectId id;

    /**
     * Name of category
     */
    @NotEmpty
    private String name;
}
