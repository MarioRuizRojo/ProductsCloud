
import ProductClient from "../clients/ProductClient";
import ProductService from "./ProductService";
import { category01, product01, threeCategories, threeProducts, twoCategories, twoOptionCategory } from "../fixtures/fixtures";
import { $productEditor } from "../hooks/productEditorHook";
import { of } from "rxjs";
import { Product } from "../models/Product";
import { Option } from "../models/Option";
import { $products } from "../hooks/productsHook";
import ProductEditorState from "../hooks/ProductEditorState";

let productEditorState : ProductEditorState;
const editorStateIni = {
    loading : true,
    editedProduct: new Product(undefined),
    formValid: false,
    editing: false,
    categories: []
};
jest.mock('../clients/ProductClient');

beforeEach(function():void{
    ProductClient.getCategories = jest.fn(() => of(threeCategories));
    ProductClient.getProducts = jest.fn(() => of(threeProducts));
    ProductClient.deleteProduct = jest.fn(() => of(true));
    ProductClient.addProduct = jest.fn(() => of(product01));
    ProductClient.updateProduct = jest.fn(() => of(product01));
    productEditorState = new ProductEditorState(editorStateIni);
});

it('getAllCategoryOptions ok',function(done):void{
    ProductService.getAllCategoryOptions(productEditorState).subscribe(categoriesOpt=>{
        expect(categoriesOpt[0].getValue()).toBe(threeCategories[0].getId());
        done();
    });        
});

it('getAllCategoryOptions state ok',function(done):void{    
    $productEditor.subscribe(newState=>{
       expect(newState.categories[0].getId()).toBe(threeCategories[0].getId());
       done();
    });     
    ProductService.getAllCategoryOptions(productEditorState).subscribe();
});

it('getAllProducts ok',function(done):void{    
    $products.subscribe(products=>{
       expect(products[0].getId()).toBe(threeProducts[0].getId());
       done();
    });     
    ProductService.getAllProducts().subscribe();
});

it('deleteSelectedProduct ok',function(done):void{  
    ProductService.deleteSelectedProduct(product01).subscribe(res=>{
        expect(ProductClient.deleteProduct).toHaveBeenCalled();
        done();
    });
});

it('deleteSelectedProduct state ok',function(done):void{  
    $products.subscribe(products=>{
        expect(products[0].getId()).toBe(threeProducts[0].getId());
        done();
     }); 
    ProductService.deleteSelectedProduct(product01).subscribe();
});

it('addProduct ok',function(done):void{  
    productEditorState.editing=false;
    productEditorState.editedProduct=product01;
    ProductService.addOrUpdateProductAndRefresh(productEditorState).subscribe(res=>{
        expect(ProductClient.addProduct).toHaveBeenCalled();
        done();
    });
});

it('updateProduct ok',function(done):void{  
    productEditorState.editing=true;
    productEditorState.editedProduct=product01;
    ProductService.addOrUpdateProductAndRefresh(productEditorState).subscribe(res=>{
        expect(ProductClient.updateProduct).toHaveBeenCalled();
        done();
    });
});

it('addOrUpdateProductAndRefresh state ok',function(done):void{
    $products.subscribe(products=>{
        expect(products[0].getId()).toBe(threeProducts[0].getId());
        done();
     });
    productEditorState.editedProduct=product01;
    ProductService.addOrUpdateProductAndRefresh(productEditorState).subscribe();
});

it('editSelectedProduct state ok',function(done):void{
    $productEditor.subscribe(newState=>{
        expect(newState.editedProduct.getId()).toBe(product01.getId());
        done();
     });  
    productEditorState.editedProduct=product01;
    ProductService.editSelectedProduct(product01, productEditorState);
});

it('changeInputField name in state ok',function(done):void{
    const newName = 'newName';
    $productEditor.subscribe(newState=>{
        expect(newState.editedProduct.getName()).toBe(newName);
        done();
     });
    productEditorState.editedProduct=product01;
    const ev : any = {target:{name:'nameName', value:newName}};
    ProductService.changeInputField(ev,'idName',productEditorState);
});

it('changeInputField price in state ok',function(done):void{
    const newPrice = '1.2';
    $productEditor.subscribe(newState=>{
        expect(newState.editedProduct.getPrice()).toBe(1.2);
        done();
     });
    productEditorState.editedProduct=product01;
    const ev : any = {target:{name:'namePrice', value:newPrice}};
    ProductService.changeInputField(ev,'idPrice',productEditorState);
});

it('selectCategory ok',function(done):void{
    productEditorState.categories=threeCategories;
    $productEditor.subscribe(newState=>{
        expect(newState.editedProduct.getCategoryDTO().getId()).toBe(category01.getId());
        done();
     });
    const optionSelected : any = new Option({value:category01.getId(),label:category01.getName()});
    ProductService.selectCategory(productEditorState,optionSelected);
});

it('getOptionCategory ok',function():void{
    productEditorState.categories=twoCategories;
    const actual : Option = ProductService.getOptionCategory(twoOptionCategory,category01);
    expect(actual.getValue()).toBe(category01.getId());
});
