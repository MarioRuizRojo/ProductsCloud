package com.springboot.webflux.products.model.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author Mario Ruiz Rojo
 * Employee
 */
@Data
public class Employee {
    /**
     * Identifier of employee
     */
    private String id;

    /**
     * Name of employee
     */
    private String name;

    /**
     * Last name of employee
     */
    private String lastName;

    /**
     * Creation date of employee record
     */
    private LocalDateTime createdAt;

    /**
     * Company of employee
     */
    private Company company;
}
