package com.springboot.webflux.productscurrency.controller;

import com.springboot.webflux.productscurrency.model.dto.ProductDTO;
import com.springboot.webflux.productscurrency.model.dto.ResponseDTO;
import com.springboot.webflux.productscurrency.service.ProductService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Products CRUD API Controller with currency exchange calculation for prices
 */
@RestController
@CrossOrigin(origins = "${frontendurl}")
@RequestMapping("/${config.urlProducts}")
public class ProductController {

    /**
     * Products Service
     */
    private ProductService productService;

    public ProductController(ProductService productService){
            this.productService = productService;
    }    

    /**
     * It returns the list of products with prices in dollars
     * @return list of products
     */
    @GetMapping("/")
    public Mono<ResponseDTO> list(){
        return productService.getAll();
    }

    /**
     * It returns the list of categories
     * @return list of categories
     */
    @GetMapping("/${config.urlCategories}")
    public Mono<ResponseDTO> categories(){
        return productService.getAllCategories();
    }

    /**
     * It returns a product by id with price in dollars
     * @return product
     */
    @GetMapping("/{id}")
    public Mono<ResponseDTO> details(@PathVariable String id){
        return productService.getById(id);
    }

    /**
     * It inserts product
     * It generates the creation date of the product
     * It returns the generated product with price in dollars
     * @return product
     */
    @PostMapping("/")
    public Mono<ResponseDTO> create(@RequestBody ProductDTO productDTO){
        return productService.insertNew(productDTO);
    }

    /**
     * Updates product by id
     * returns the product updated with price in dollars
     * @return product
     */
    @PutMapping("/")
    public Mono<ResponseDTO> edit(@RequestBody ProductDTO productDTO){
        return productService.update(productDTO);
    }

    /**
     * It deletes a product by id
     * @return no content if found
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseDTO> delete(@PathVariable String id){
        return productService.delete(id);
    }
}
