package com.springboot.webflux.products.model.dao.employeedb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @author Mario Ruiz Rojo
 * It represents employees collection
 */
@Document(collection="employees")
@Data
public class EmployeeDAO {
    /**
     * Identifier of employee
     */
    @NotNull
    @Id
    private ObjectId id;

    /**
     * Name of employee
     */
    @NotEmpty
    private String name;

    /**
     * Last name of employee
     */
    @NotEmpty
    @Field("last_name")
    private String lastName;

    /**
     * Creation date of employee record
     */
    @DateTimeFormat(pattern="YYYY-MM-DD[T]HH:mm:ss.SSS[Z]")
    @Field("created_at")
    private Date createdAt;

    /**
     * Company of employee
     */
    @Valid
    @NotNull
    //@Field("company")
    private CompanyDAO company;

}


