import { Category } from "./Category";
import { Product } from "./Product";

/**
 * ResponseDTO from server
 */
export class ResponseDTO{
    /**
     * Response's product list
     */
    private productsDTO: Product[];
    /**
     * Response's category list
     */
    private categoriesDTO: Category[];
    /**
     * Response's error of field list
     */
    private fieldErrors: string[];
    /**
     * Response's error message
     */
    private errorMsg: string;

    /**
     * Constructor from jsonObject
     * @param response jsonObject
     */
    constructor(responseDTO : any){
        if(responseDTO===undefined){
            this.productsDTO = [];
            this.categoriesDTO = [];
            this.fieldErrors = [];
            this.errorMsg = '';
        }else{
            if(responseDTO.productsDTO!==undefined){
                this.productsDTO = responseDTO.productsDTO.map((product:any) => new Product(product));
            }
            else{
                this.productsDTO = [];
            }
            if(responseDTO.categoriesDTO!==undefined){
                this.categoriesDTO = responseDTO.categoriesDTO.map((category:any) => new Category(category));
            }
            else{
                this.categoriesDTO = [];
            }
            if(responseDTO.fieldErrors!==undefined){
                this.fieldErrors = responseDTO.fieldErrors.map((fieldError:any) => fieldError as string);
            }
            else{
                this.fieldErrors = [];
            }            
            this.errorMsg = responseDTO.errorMsg as string;
        }        
    }

    /**
     * 
     * @returns list of products
     */
    public getProductsDTO(): Product[] {
        return this.productsDTO;
    }

    /**
     * 
     * @param value to set list of products
     */
    public setProductsDTO(products: Product[]) {
        this.productsDTO = products;
    }

    /**
     * 
     * @returns list of categories
     */
    public getCategoriesDTO(): Category[] {
        return this.categoriesDTO;
    }

    /**
     * 
     * @param value to set list of categories
     */
    public setCategoriesDTO(categories: Category[]) {
        this.categoriesDTO = categories;
    }

    /**
     * 
     * @returns list of product's fields errors
     */
    public getFieldErrors(): string[] {
        return this.fieldErrors;
    }

    /**
     * 
     * @param value to set list of product's fields errors
     */
    public setFieldErrors(fieldErrors: string[]) {
        this.fieldErrors = fieldErrors;
    }

    /**
     * 
     * @returns error message
     */
    public getErrorMsg(): string {
        return this.errorMsg;
    }

    /**
     * 
     * @param value to set error message
     */
    public setErrorMsg(errorMsg: string) {
        this.errorMsg = errorMsg;
    }
}