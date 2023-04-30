package com.springboot.webflux.products.service;

import com.springboot.webflux.products.model.ProductUtils;
import com.springboot.webflux.products.model.ResponseUtils;
import com.springboot.webflux.products.model.bo.Product;
import com.springboot.webflux.products.model.dto.ProductDTO;
import com.springboot.webflux.products.model.dto.ResponseDTO;
import com.springboot.webflux.products.model.mapper.CategoryDAOMapper;
import com.springboot.webflux.products.model.mapper.CategoryDTOMapper;
import com.springboot.webflux.products.model.mapper.ProductDAOMapper;
import com.springboot.webflux.products.model.mapper.ProductDTOMapper;
import com.springboot.webflux.products.repository.productdb.CategoryRepository;
import com.springboot.webflux.products.repository.productdb.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 *
 * @author Mario Ruiz Rojo
 * All product CRUD API endpoints service implementation
 */
@Service
public class ProductService {
    /**
     * Product collection repository
     */
    private final ProductRepository productRepository;
    /**
     * Category collection repository
     */
    private final CategoryRepository categoryRepository;

    /**
     * Validator of ProductDTO
     */
    private final Validator validator;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            Validator validator
    ){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    //----------PRIVATE----------

    /**
     * It runs validation on ProductDTO and converts it to Product 
     * @param productReceived
     * @return Product
     */
    private Mono<Product> validateProductReceived(ProductDTO productReceived){
        return ResponseUtils.validateProduct(productReceived,validator)
                .map(ProductDTOMapper.INSTANCE::DTOtoBO);
    }

    /**
     * It converts Product into ProductDAO and checks if it exists already in DB
     * @param product
     * @return exception if it doesn't exist or Product otherwise
     */
    private Mono<Product> existsById(Product product){
        return Mono.just(product)
                .map(ProductDAOMapper.INSTANCE::BOtoDAO)
                .flatMap(prodDao->productRepository.existsById(prodDao.getId()))
                .flatMap((Boolean exists)->{
                    if(exists){
                        return Mono.just(product);

                    }
                    else{
                        return Mono.error(new Exception("not found"));
                    }
                });
    }

    //----------PUBLIC----------

    /**
     * Fetch all Products from products collection, add them to the API Response 
     * @return API Response
     */
    public Mono<ResponseDTO> getAll() {
        return productRepository.findAll()
                .flatMap(ProductUtils::fromDAOtoDTO)
                .collectList()
                .map(ResponseUtils::fromProductDTOListToResponseDTO)
                .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * Fetch all Categories from categories collection, add them to the API Response 
     * @return API Response
     */
    public Mono<ResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .map(CategoryDAOMapper.INSTANCE::DAOtoBO)
                .map(CategoryDTOMapper.INSTANCE::BOtoDTO)
                .collectList()
                .map(ResponseUtils::fromCategoryDTOListToResponseDTO)
                .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * It finds one Product by id from products collection, and adds it to the API Response 
     * @return API Response
     */
    public Mono<ResponseDTO> getById(String id) {
        return productRepository.findById(new ObjectId(id))
                .switchIfEmpty(Mono.error(new Exception("not found")))
                .flatMap(ProductUtils::fromDAOtoDTO)
                .map(ResponseUtils::fromProductDTOtoResponseDTO)
                .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * It validates one Product then inserts it in products collection
     * and adds it to the API Response 
     * @return API Response
     */
    public Mono<ResponseDTO> insertNew(ProductDTO productReceived) {
        return validateProductReceived(productReceived)
                .map(productCreated -> {
                    productCreated.setCreatedAt(LocalDateTime.now());
                    return productCreated;
                })
                .map(ProductDAOMapper.INSTANCE::BOtoDAO)
                .flatMap(productRepository::save)
                .flatMap(ProductUtils::fromDAOtoDTO)
                .map(ResponseUtils::fromProductDTOtoResponseDTO)
                .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * It validates one Product, checks if it exists, then updates it in products collection
     * and adds it to the API Response 
     * @return API Response with error msg in case it doesn't exist
     */
    public Mono<ResponseDTO> update(ProductDTO productEdited) {
        return validateProductReceived(productEdited)
                .flatMap(this::existsById)
                .map(ProductDAOMapper.INSTANCE::BOtoDAO)
                .flatMap(productRepository::save)
                .flatMap(ProductUtils::fromDAOtoDTO)
                .map(ResponseUtils::fromProductDTOtoResponseDTO)
                .onErrorResume(ResponseUtils::responseErrors);
    }

    /**
     * It checks if id exists in products collection, then delete that document
     * and sends back an ACK as API Response 
     * @return API Response with error msg in case it doesn't exist
     */
    public Mono<ResponseDTO> delete(String id) {
        ObjectId Oid = new ObjectId(id);
        return productRepository.existsById(Oid)
                .flatMap(exists->{
                    if(exists) {
                        return productRepository.deleteById(Oid).then(Mono.just(true));
                    }
                    else{
                        return Mono.error(new Exception("not found"));
                    }
                })
                .map(r->ResponseUtils.emptyResponse())
                .onErrorResume(ResponseUtils::responseErrors);
    }
}
