import { AxiosResponse } from 'axios';
import baseClient from './baseAxios';
import { Observable, from, map, of, switchMap } from 'rxjs';

import { Category } from '../models/Category';
import { Product } from '../models/Product';
import { Utils } from '../Utils';
import { ResponseDTO } from '../models/ResponseDTO';
import { API_CATEGORIES, API_PRODUCTS } from '../constants';

/**
 * Server's client for product requests
 */
export default class ProductClient{    

    /**
     * It converts an AxiosResponse into a ResponseDTO
     * @param AxiosResponse 
     * @returns ResponseDTO
     */
    private static fromAxiosToResponseDTO(axiosResponse: AxiosResponse): Observable<ResponseDTO>{
        return of(axiosResponse).pipe(
            map((axiosResponse: AxiosResponse)=>axiosResponse.data),
            map((object: any) => new ResponseDTO(object)),
        );
    }

    /**
     * It returns the first product inside the ResponseDTO products array
     */
    private static getProductFromResponse(axiosResponse: AxiosResponse): Observable<Product>{
        return ProductClient.fromAxiosToResponseDTO(axiosResponse).pipe(
            map((responseDTO: ResponseDTO)=>responseDTO.getProductsDTO()),
            map((products:Product[])=>{
                if(products.length){
                    return products[0];
                }
                else{
                    throw new Error('The response has not product');
                }
            })
        );
    }

    //------------PUBLIC------------
    /**
     * It requests the categories of product to the API
     * It does conversion from axiosResponse to category array
     * @returns list of categories
     */
    public static getCategories(): Observable<Category[]>{
        return from(baseClient.get(API_CATEGORIES)).pipe(
            switchMap(ProductClient.fromAxiosToResponseDTO),
            map((responseDTO: ResponseDTO)=>responseDTO.getCategoriesDTO())
        );
    }

    /**
     * It requests all products to the API
     * It does conversion from axiosResponse to product array
     * @returns list of products
     */
    public static getProducts(): Observable<Product[]>{
        return from(baseClient.get(API_PRODUCTS)).pipe(
            switchMap(ProductClient.fromAxiosToResponseDTO),
            map((responseDTO: ResponseDTO)=>responseDTO.getProductsDTO())
        );
    }

    /**
     * It request to save a product in db to the API
     * It does conversion from axiosResponse to product
     * @param product to save in db
     * @returns the saved product in db
     */
    public static addProduct(product: Product): Observable<Product>{
        return of(product).pipe(
            map((prod: Product) =>Utils.checkIfParameterIsUndefined(prod,'product to save cannot be empty')),
            switchMap((prod: Product) =>from(baseClient.post(API_PRODUCTS, prod))),
            switchMap(ProductClient.getProductFromResponse),
        );
    }

    /**
     * It request to update a product in db to the API
     * It does conversion from axiosResponse to product
     * @param product to update in db
     * @returns the updated product in db
     */
    public static updateProduct(product: Product): Observable<Product>{
        return of(product).pipe(
            map((prod: Product) =>Utils.checkIfParameterIsUndefined(prod,'product to update cannot be empty')),
            switchMap((prod: Product) =>from(baseClient.put(API_PRODUCTS, prod))),
            switchMap(ProductClient.getProductFromResponse),
        );
    }

    /**
     * It request to delete a product in db to the API
     * It does conversion from axiosResponse to boolean
     * @param product id to delete in db
     * @returns true if deleted
     */
    public static deleteProduct(identifier: string): Observable<boolean>{
        return of(identifier).pipe(
            map((id: string) =>Utils.checkIfParameterIsUndefined(id,'id of product to delete cannot be empty')),
            switchMap((id: string) =>from(baseClient.delete(API_PRODUCTS+id))),
            switchMap(ProductClient.fromAxiosToResponseDTO),
            map((response: ResponseDTO)=>
                response.getErrorMsg()===undefined || response.getErrorMsg().length===0
            )
        );
    }
}