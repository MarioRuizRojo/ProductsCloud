package com.springboot.webflux.products.controller;

import com.springboot.webflux.products.model.dto.EmployeeDTO;
import com.springboot.webflux.products.model.dto.ProductDTO;
import com.springboot.webflux.products.model.dto.ResponseDTO;
import com.springboot.webflux.products.service.ProductService;
import com.springboot.webflux.products.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Products CRUD API Controller 
 */
@RestController
@RequestMapping("/${config.urlProducts}")
public class ProductController {

    /**
     * Products Service
     */
    private ProductService productService;

    /**
     * Employees Service
     */
    private final EmployeeService employeeService;

    public ProductController(ProductService productService, 
        EmployeeService employeeService){
            this.employeeService = employeeService;
            this.productService = productService;
    }    

    /**
     * It returns the list of products from db
     * @return list of products
     */
    @GetMapping("/")
    public Mono<ResponseDTO> list(){
        return productService.getAll();
    }

    /**
     * It returns the list of categories from db
     * @return list of categories
     */
    @GetMapping("/${config.urlCategories}")
    public Mono<ResponseDTO> categories(){
        return productService.getAllCategories();
    }

    /**
     * It returns a product by id from db
     * @return product
     */
    @GetMapping("/{id}")
    public Mono<ResponseDTO> details(@PathVariable String id){
        return productService.getById(id);
    }

    /**
     * It inserts product in db
     * It generates the creation date of the product
     * It returns the generated product
     * @return product
     */
    @PostMapping("/")
    public Mono<ResponseDTO> create(@RequestBody ProductDTO productDTO){
        return productService.insertNew(productDTO);
    }

    /**
     * Updates product in db by id
     * returns the product updated
     * @return product
     */
    @PutMapping("/")
    public Mono<ResponseDTO> edit(@RequestBody ProductDTO productDTO){
        return productService.update(productDTO);
    }

    /**
     * It deletes a product in mongodb by id
     * @return no content if found
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseDTO> delete(@PathVariable String id){
        return productService.delete(id);
    }

    /**
     * Endpoint to test Employees Service
     * @return all employees
     */
    @GetMapping("/employees")
    public Flux<EmployeeDTO> getUsAll(){
        return employeeService.getAll();
    }
}
