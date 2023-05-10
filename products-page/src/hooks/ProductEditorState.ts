import { Category } from "../models/Category";
import { Product } from "../models/Product";

/**
 * Product editor state data
 */
export default class ProductEditorState{
    /**
     * waiting for API response
     */
    loading : boolean;
    /**
     * edition values for new product or update existing one
     */
    editedProduct : Product;
    /**
     * edited product follows name and price constraints
     */
    formValid: boolean;
    /**
     * user is editing an existing product, false if creating a new one
     */
    editing: boolean;
    /**
     * categories to display in selector
     */
    categories: Category[];

    constructor(obj:any){
        if(obj){
            this.loading=obj.loading;
            this.formValid=obj.formValid;
            this.editing=obj.editing;
            this.editedProduct= new Product(obj.editedProduct);
            this.categories = obj.categories.map((category:any)=>new Category(category));
        }
        else{
            this.loading = true;
            this.formValid = true;
            this.editing = false;
            this.editedProduct = new Product(undefined);
            this.categories = [];
        }        
    }
}