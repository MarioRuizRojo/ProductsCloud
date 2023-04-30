package com.springboot.webflux.products.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 *
 * @author Mario Ruiz Rojo
 * Employee
 */
@Data
public class EmployeeDTO {
    /**
     * Identifier of employee
     */
    @NotEmpty
    private String id;

    /**
     * Name of employee
     */
    @NotEmpty
    private String name;

    /**
     * Last name of employee
     */
    @NotEmpty
    private String lastName;

    /**
     * Creation date of employee record
     */
    @DateTimeFormat(pattern="YYYY-MM-DD[T]HH:mm:ss.SSS[Z]")
    @NotNull
    private LocalDateTime createdAt;

    /**
     * Company of employee
     */
    @Valid
    @NotNull
    private CompanyDTO companyDTO;
}