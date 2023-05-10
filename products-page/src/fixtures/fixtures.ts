import { Category } from "../models/Category";
import { Product } from "../models/Product";
import { Option } from "../models/Option";
import { ResponseDTO } from "../models/ResponseDTO";

/**
 * JSON data to create option categories
 */
export let jsonOptionCategory01 : any = {value:'id01',label:'name01'};
export let jsonOptionCategory02 : any = {value:'id02',label:'name02'};
/**
 * 2 sample option categories
 */
export let optionCategory01 : Option = new Option(jsonOptionCategory01);
export let optionCategory02 : Option = new Option(jsonOptionCategory02);
/**
 * 2 sample list of option categories
 */
export const twoOptionCategory : Option[] = [optionCategory01,optionCategory02];
/**
 * JSON data to create categories
 */
export const jsonCategory01 : any = {id:'id01',name:'name01'};
export const jsonCategory02 : any = {id:'id02',name:'name02'};
export const jsonCategory03 : any = {id:'id03',name:'name03'};
/**
 * 3 sample categories
 */
export const category01 : Category = new Category(jsonCategory01);
export const category02 : Category = new Category(jsonCategory02);
export const category03 : Category = new Category(jsonCategory03);

export const dateFixed : Date = new Date(Date.UTC(1995, 4, 23));
/**
 * JSON data to create products
 */
export const jsonProduct01 : any = {id:'id01',name:'name01',price:1,createdAt:dateFixed,categoryDTO:category01,picture:'picture01'};
export const jsonProduct02 : any = {id:'id02',name:'name02',price:2.56,createdAt:dateFixed,categoryDTO:category02,picture:'picture02'};
export const jsonProduct03 : any = {id:'id03',name:'name03',price:3.3,createdAt:dateFixed,categoryDTO:category01,picture:'picture03'};
/**
 * 3 sample products
 */
export const product01 : Product = new Product(jsonProduct01);
export const product02 : Product = new Product(jsonProduct02);
export const product03 : Product = new Product(jsonProduct03);

/**
 * 2 sample list of categories
 */
export const twoCategories : Category[] = [category01,category02];
export const threeCategories : Category[] = [category01,category02,category03];

/**
 * 3 sample list of products
 */
export const oneProduct : Product[] = [product01];
export const twoProducts : Product[] = [product01,product02];
export const threeProducts : Product[] = [product01,product02,product03];

export const twoFieldErrors : string[] = ['name is empty','price is negative'];

/**
 * Response sample with list of products
 */
const responseWithProdsJson: any = {
    categoriesDTO:[], productsDTO:threeProducts,
    fieldErrors:twoFieldErrors, errorMsg: 'some generic error'
};
/**
 * Response sample with list of categories
 */
const responseWithCatsJson: any = {
    categoriesDTO:threeCategories, productsDTO:[],
    fieldErrors:twoFieldErrors, errorMsg: 'some generic error'
};
/**
 * Response sample with list of products and list of categories
 */
const responseFullJson: any = {
    categoriesDTO:threeCategories, productsDTO:threeProducts,
    fieldErrors:twoFieldErrors, errorMsg: 'some generic error'
};
/**
 * Response sample empty
 */
const responseEmptyJson: any = {
    categoriesDTO:[], productsDTO:[],
    fieldErrors:[], errorMsg: ''
};
/**
 * Four different responses
 */
export const responseWithProducts : ResponseDTO = new ResponseDTO(responseWithProdsJson);
export const responseWithCategories : ResponseDTO = new ResponseDTO(responseWithCatsJson);
export const responseFull : ResponseDTO = new ResponseDTO(responseFullJson);
export const responseEmpty : ResponseDTO = new ResponseDTO(responseEmptyJson);

/**
 * Delay in micro seconds for tests
 * @param micros is microseconds to wait
 */
export function waitMicroSeconds(micros:number):Promise<void>{
    return new Promise(function(resolve : any,reject : any):void{
        setTimeout(function():void{
            resolve();
        }, micros);
    });
}