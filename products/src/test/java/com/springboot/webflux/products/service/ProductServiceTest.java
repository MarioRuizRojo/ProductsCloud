package com.springboot.webflux.products.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.Validator;

import com.springboot.webflux.products.Fixtures;
import com.springboot.webflux.products.Utils;
import com.springboot.webflux.products.repository.productdb.CategoryRepository;
import com.springboot.webflux.products.repository.productdb.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    Validator validator;

    private ProductService productService;

    private LocalDateTime now;
    private ObjectId oid;

    @BeforeEach
    void setup(){        
        this.productService = new ProductService(productRepository, categoryRepository, validator);

        this.oid = Fixtures.objectId();

        this.now = LocalDateTime.now();
        when(productRepository.save(any())).thenReturn(
            Mono.just(Fixtures.oneProductDAO(this.now))
        );        

        when(productRepository.existsById(any(ObjectId.class))).thenReturn(Mono.just(true));
    }

    //------------------------OK-----------------------------
    @Test
    void get_all_products_2products(){
        when(productRepository.findAll()).thenReturn(Flux.fromIterable(Fixtures.twoProductsDAO()));
        StepVerifier.create(
            productService.getAll()
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().size()==2);
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_all_categories_2categories(){
        when(categoryRepository.findAll()).thenReturn(Flux.fromIterable(Fixtures.twoCategoriesDAO()));
        StepVerifier.create(
            productService.getAllCategories()
        )
        .consumeNextWith(r->{
            assertTrue(r.getCategoriesDTO().size()==2);
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_by_id_product_ok(){
        
        when(productRepository.findById(any(ObjectId.class))).thenReturn(
            Mono.just(Fixtures.oneProductDAO(LocalDateTime.now()))
        );
        StepVerifier.create(
            productService.getById(this.oid.toString())
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().size()==1);
            assertTrue(r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void insert_new_product_ok(){
        StepVerifier.create(
            productService.insertNew(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().size()==1);
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void update_product_ok(){      
        StepVerifier.create(
            productService.update(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().size()==1);
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void delete_product_ok(){
        when(productRepository.deleteById(any(ObjectId.class))).thenReturn(Utils.monoVoid());
        StepVerifier.create(
            productService.delete(this.oid.toString())
        )        
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    //------------------------ERROR-----------------------------
    @Test
    void get_all_products_error(){
        when(productRepository.findAll()).thenReturn(Flux.error(new Exception("no products in db")));
        StepVerifier.create(
            productService.getAll()
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_all_categories_error(){
        when(categoryRepository.findAll()).thenReturn(Flux.error(new Exception("no categories in db")));
        StepVerifier.create(
            productService.getAllCategories()
        )
        .consumeNextWith(r->{
            assertTrue(r.getCategoriesDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void get_by_id_product_notFound(){                
        when(productRepository.findById(any(ObjectId.class))).thenReturn(
            Mono.empty()
        );
        StepVerifier.create(
            productService.getById(this.oid.toString())
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void insert_new_product_error(){
        when(productRepository.save(any())).thenReturn(
            Mono.error(new Exception("db error"))
        );
        StepVerifier.create(
            productService.insertNew(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void update_product_notFound(){      
        when(productRepository.existsById(any(ObjectId.class))).thenReturn(Mono.just(false));
        StepVerifier.create(
            productService.update(Fixtures.oneProduct(this.now))
        )
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }

    @Test
    void delete_product_notFound(){
        when(productRepository.deleteById(any(ObjectId.class))).thenReturn(Utils.monoVoid());
        when(productRepository.existsById(any(ObjectId.class))).thenReturn(Mono.just(false));
        StepVerifier.create(
            productService.delete(this.oid.toString())
        )        
        .consumeNextWith(r->{
            assertTrue(r.getProductsDTO().isEmpty());
            assertTrue(r.getFieldErrors().isEmpty());
            assertTrue(!r.getErrorMsg().isEmpty());
        })
        .expectComplete()
        .verify();
    }
}
