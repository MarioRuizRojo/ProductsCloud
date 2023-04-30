package com.springboot.webflux.products;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.springboot.webflux.products.model.ProductUtils;
import com.springboot.webflux.products.model.dao.productdb.CategoryDAO;
import com.springboot.webflux.products.model.dao.productdb.ProductDAO;
import com.springboot.webflux.products.model.dto.CategoryDTO;
import com.springboot.webflux.products.model.dto.ProductDTO;
import com.springboot.webflux.products.model.dto.ResponseDTO;
import com.springboot.webflux.products.model.mapper.CategoryDAOMapper;
import com.springboot.webflux.products.model.mapper.CategoryDTOMapper;

public class Fixtures {
    public static CategoryDTO oneCategory(){
        return new CategoryDTO("64410801ce3862e9c283e98d","Electronics");
    }
    public static List<CategoryDTO> twoCategories(){
        List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
        categories.add(oneCategory());
        categories.add(new CategoryDTO("64410801ce3862e9c283e98a","Sports"));
        return categories;
    }

    public static List<CategoryDAO> twoCategoriesDAO(){
        return twoCategories().stream()
            .map(CategoryDTOMapper.INSTANCE::DTOtoBO)
            .map(CategoryDAOMapper.INSTANCE::BOtoDAO)
            .collect(Collectors.toList());
    }

    public static ProductDTO oneProduct(LocalDateTime now){
        return new ProductDTO("64410801ce3862e9c283e98e","mouse",
            1.2,now,oneCategory(),"");
    }
    public static ProductDAO oneProductDAO(LocalDateTime now){
        return ProductUtils.fromDTOtoDAO(oneProduct(now));
    }

    public static List<ProductDTO> oneProductList(){        
        LocalDateTime now = LocalDateTime.now();        
        List<ProductDTO> products = new ArrayList<ProductDTO>();
        products.add(oneProduct(now));
        return products;
    }

    public static List<ProductDTO> twoProducts(){        
        LocalDateTime now = LocalDateTime.now();        
        ProductDTO p2 = new ProductDTO("64410801ce3862e9c283e990","keyboard",
            3.1,now,oneCategory(),"");
        List<ProductDTO> products = new ArrayList<ProductDTO>();
        products.add(oneProduct(now));
        products.add(p2);
        return products;
    }

    public static List<ProductDAO> twoProductsDAO(){
        return twoProducts().stream()
            .map(ProductUtils::fromDTOtoDAO)
            .collect(Collectors.toList());
    }

    public static ResponseDTO response2products(){
        return new ResponseDTO(twoProducts(), Utils.emptyList(), 
            Utils.emptyList(), "");
    }

    public static ResponseDTO response2categories(){
        return new ResponseDTO(Utils.emptyList(), twoCategories(), 
            Utils.emptyList(), "");
    }

    public static ObjectId objectId(){
        return new ObjectId("64410801ce3862e9c283e98e");
    }
    public static ResponseDTO responseOneProduct() {
        return new ResponseDTO(oneProductList(), Utils.emptyList(), 
            Utils.emptyList(), "");
    }
}
