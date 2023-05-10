import { SingleValue, ActionMeta } from "react-select";
import { Observable, catchError, from, map, of, switchMap, tap, toArray } from "rxjs";

import { Option } from "../models/Option";
import { Product } from "../models/Product";
import { Category } from "../models/Category";
import { $productEditor } from "../hooks/productEditorHook";
import { OPTION_DEFAULT } from "../constants";
import ProductClient from "../clients/ProductClient";
import { $products } from "../hooks/productsHook";
import ProductEditorState from "../hooks/ProductEditorState";

/**
 * Service to manage products
 */
export default class ProductService{

    /**
     * It shows error on console
     * @param error to show
     * @returns void
     */
    private static handleError(err:Error):Observable<void>{
        console.error(err);
        return of();
    }

    /**
     * It sets list of categories in ProductEditor's state (editor form)
     * @param categories 
     * @param productEditorState 
     */
    private static setCategoriesInEditor(categories:Category[], productEditorState: ProductEditorState):void{
        let newProductEditorState = new ProductEditorState(productEditorState);
        newProductEditorState.categories = categories;
        newProductEditorState.loading = false;
        $productEditor.next(newProductEditorState);
    }

    /**
     * It resets the ProductEditor's state (editor form)
     * @param productEditorState 
     */
    private static resetEditorProduct(productEditorState: ProductEditorState):void{
        let newProductEditorState = new ProductEditorState(productEditorState);
        newProductEditorState.editedProduct = new Product(undefined);
        newProductEditorState.editing=false;
        newProductEditorState.formValid=true;
        newProductEditorState.loading=false;
        $productEditor.next(newProductEditorState);
    }

    /**
     * it calls ProductClient to update or create 
     * the product's data from ProductEditor's state (editor form)
     * It sets dummy Id and dummy date to create a new product
     * @param productEditorState 
     * @returns 
     */
    private static addOrUpdateProduct(productEditorState: ProductEditorState):Observable<Product>{
        const product : Product = productEditorState.editedProduct;       
        const isUpdateOp : boolean = productEditorState.editing;
        if(isUpdateOp){
            return ProductClient.updateProduct(product);
        }
        else{
            product.setId('0801ce3862e9c283');
            product.setCreatedAt(new Date(Date.UTC(1995, 4, 23)));
            return ProductClient.addProduct(product);
        }
    }

    //------------PUBLIC------------
    //------------ASYNC------------
    
    /**
     * It gets category list from server, 
     * set them into the form selector (in editor's form)
     * and  converts it to selector options 
     * @param productEditorState 
     * @returns categories selector options
     */
    public static getAllCategoryOptions(productEditorState: ProductEditorState):Observable<Option[]> {
        return ProductClient.getCategories().pipe(
            tap((categories:Category[])=>ProductService.setCategoriesInEditor(categories,productEditorState)),
            switchMap(categories=>from(categories)),
            map((category:Category)=>new Option({value:category.getId(), label:category.getName()})),
            toArray(),
            catchError(e=>{console.error(e);return of([]);})
        );
    }

    /**
     * It request deletion of product by id to the server
     * and refresh the list of products
     * @param product 
     * @returns void
     */
    public static deleteSelectedProduct(product: Product):Observable<void>{
        return ProductClient.deleteProduct(product.getId()).pipe(
            map((result:boolean)=>{
                if(!result){
                    throw new Error('Cannot delete seleted product');                    
                }
                return true;
            }),
            switchMap(r=>ProductService.getAllProducts()),
            catchError(e=>ProductService.handleError(e))
        );
    }

    /**
     * It request an update or insertion of the form's product,
     * resets the form and refresh list of products
     * @param productEditorState 
     * @returns void
     */
    public static addOrUpdateProductAndRefresh(productEditorState: ProductEditorState):Observable<void>{
        return ProductService.addOrUpdateProduct(productEditorState).pipe(
            tap(r=>ProductService.resetEditorProduct(productEditorState)),
            switchMap(r=>ProductService.getAllProducts()),
            catchError(e=>ProductService.handleError(e))
        );
    }

    /**
     * It request list of products to the server and refresh the list in the view
     * @returns 
     */
    public static getAllProducts():Observable<void>{
        return ProductClient.getProducts().pipe(
            map((products:Product[])=>$products.next(products)),
            catchError(e=>ProductService.handleError(e))
        );
    }

    //------------SYNC------------
    /**
     * It refresh all data in editor's form with this product's data
     * @param product's data to set in form
     * @param productEditorState, new form state
     */
    public static editSelectedProduct(product: Product, productEditorState: ProductEditorState):void{
        let newProductEditorState = new ProductEditorState(productEditorState);
        newProductEditorState.editedProduct=product;
        newProductEditorState.editing=true;
        $productEditor.next(newProductEditorState);
    }

    /**
     * It handles changes on the form's inputs
     * and refresh the editor's form
     * also performs a validation
     * @param event 
     * @param inputId 
     * @param productEditorState 
     */
    public static changeInputField(
        event:React.ChangeEvent<HTMLInputElement>, 
        inputId: string,
        productEditorState : ProductEditorState
        ):void{
        const name : string = event.target.name;
        const value : string = event.target.value;
        let newProductEditorState = new ProductEditorState(productEditorState);
        const editedProduct : Product = newProductEditorState.editedProduct;
        switch(name){
            case 'nameName':
                editedProduct.setName(value);
                break;
            case 'namePrice':
                const price : number = value.length>0?parseFloat(value):0;
                editedProduct.setPrice(price);
                break;
            default:
        }
        newProductEditorState.formValid = (editedProduct.getName().length>0) 
            && (editedProduct.getPrice()>0.0);
        $productEditor.next(newProductEditorState);
    }

    /**
     * It handles selections on the form's dropdown
     * and refresh the editor's form state
     * @param productEditorState 
     * @param optionSelected 
     */
    public static selectCategory(
        productEditorState : ProductEditorState, 
        optionSelected: SingleValue<Option>
        ): void {
        const id : string = (new Option(optionSelected)).getValue();
        let category : Category | undefined = productEditorState.categories.find(
            (category:Category)=>category.getId()===id
        );
        if(!category){
            category = new Category(undefined);
        }
        let newProductEditorState = new ProductEditorState(productEditorState);
        newProductEditorState.editedProduct.setCategoryDTO(category);
        $productEditor.next(newProductEditorState);
    }

    /**
     * It returns the selector option related to that category
     * @param categoryOptions, selector options currently available
     * @param category, category to look for
     * @returns selector option related
     */
    public static getOptionCategory(categoryOptions: Option[], category: Category): Option {
        const option : Option | undefined = categoryOptions.find(
            (opti:Option)=>opti.value===category.getId()
        );
        if(option){
            return option;
        }
        return OPTION_DEFAULT;
    }
}