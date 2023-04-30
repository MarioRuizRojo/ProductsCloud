package com.springboot.webflux.products.model.dao.productdb;

import java.util.Date;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

//sudo service mongod start
/**
 *
 * @author Mario Ruiz Rojo
 * It represents products collection
 */
@Document(collection="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDAO {
    /**
     * Identifier of product
     */
    @NotNull
    @Id
    private ObjectId id;

    /**
     * Name of product
     */
    @NotEmpty
    private String name;

    /**
     * Price of product
     */
    @NotNull
    private Double price;

    /**
     * Creation date of product
     */
    @DateTimeFormat(pattern="YYYY-MM-DD[T]HH:mm:ss.SSS[Z]")
    @Field("created_at")
    private Date createdAt;

    /**
     * Category of product
     */
    @Valid
    @NotNull
    //@Field("category")
    private CategoryDAO category;

    /**
     * Picture file name of product
     */
    private String picture;

}


