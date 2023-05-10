import { Utils } from "../Utils";
import { responseFull, threeCategories, threeProducts, twoCategories, twoFieldErrors, twoProducts } from "../fixtures/fixtures";
import { ResponseDTO } from "./ResponseDTO";

let responseDTO : ResponseDTO;
/**
 * It creates a response with fixed categories and products
 */
beforeEach(function():void{    
    responseDTO = new ResponseDTO(Utils.clone(responseFull));
});

/**
 * It expects to not throw error if constructor parameter is undefined
 */
it('constructor doesnt crash if parameter is undefined',function():void{
    let creation = function():void{
        new ResponseDTO(undefined);
    }
    expect(creation).not.toThrow(Error);
});

/**
 * It expects to get the same categories setted in constructor
 */
it('getCategories what it setted in constructor before',function():void{    
    const categories = responseDTO.getCategoriesDTO();
    expect(categories[0].getId()).toBe(threeCategories[0].getId());
});

/**
 * It expects to get the same products setted in constructor
 */
it('getProducts what it setted in constructor before',function():void{  
    const products = responseDTO.getProductsDTO(); 
    expect(products[0].getId()).toBe(threeProducts[0].getId());
});

/**
 * It expects to get the same field errors setted in constructor
 */
it('getFieldErrors what it setted in constructor before',function():void{ 
    const fieldErrors = responseDTO.getFieldErrors();
    expect(fieldErrors[0]).toBe(twoFieldErrors[0]);
});

/**
 * It expects to get the same products setted just before
 */
it('getProducts what it setted with set before',function():void{ 
    responseDTO.setProductsDTO(twoProducts);
    const id = responseDTO.getProductsDTO()[1].getId();
    expect(id).toBe(twoProducts[1].getId());
    expect(responseDTO.getProductsDTO().length).toBe(twoProducts.length);
});

/**
 * It expects to get the same categories setted just before
 */
it('getCategories what it setted with set before',function():void{ 
    responseDTO.setCategoriesDTO(twoCategories);
    const id = responseDTO.getCategoriesDTO()[1].getId();
    expect(id).toBe(twoCategories[1].getId());
    expect(responseDTO.getCategoriesDTO().length).toBe(twoCategories.length);
});

/**
 * It expects to get the same field errors setted just before
 */
it('getFieldErrors what it setted with set before',function():void{ 
    responseDTO.setFieldErrors([]);
    expect(responseDTO.getFieldErrors().length).toBe(0);
});

/**
 * It expects to get the same error message setted just before
 */
it('getErrorMsg what it setted with set before',function():void{
    const errMsg = 'some error';
    responseDTO.setErrorMsg(errMsg);
    expect(responseDTO.getErrorMsg()).toBe(errMsg);
});