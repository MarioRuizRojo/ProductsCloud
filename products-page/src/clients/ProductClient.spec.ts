import baseClient from './baseAxios';

import ProductClient from "./ProductClient";
import { product01, responseEmpty, responseWithCategories, responseWithProducts, threeCategories, threeProducts } from "../fixtures/fixtures";

jest.mock('./baseAxios');
const mockedBaseClient = jest.mocked(baseClient,true);

beforeEach(function():void{
    mockedBaseClient.post.mockResolvedValueOnce({status:200, data: responseWithProducts});
    mockedBaseClient.put.mockResolvedValueOnce({status:200, data: responseWithProducts});
    mockedBaseClient.delete.mockResolvedValueOnce({status:200, data: responseEmpty});
});

it('getCategories ok',function(done):void{
    mockedBaseClient.get.mockResolvedValueOnce({status:200, data: responseWithCategories});
    ProductClient.getCategories().subscribe(categories=>{
        expect(categories[0].getId()).toBe(threeCategories[0].getId());
        done();
    });
});

it('getProducts ok',function(done):void{
    mockedBaseClient.get.mockResolvedValueOnce({status:200, data: responseWithProducts});
    ProductClient.getProducts().subscribe(products=>{
        expect(products[0].getId()).toBe(threeProducts[0].getId());
        done();
    });
});

it('addProduct ok',function(done):void{
    ProductClient.addProduct(product01).subscribe(product=>{
        expect(product.getId()).toBe(threeProducts[0].getId());
        done();
    });
});

it('updateProduct ok',function(done):void{
    ProductClient.updateProduct(product01).subscribe(product=>{
        expect(product.getId()).toBe(threeProducts[0].getId());
        done();
    });
});

it('deleteProduct ok',function(done):void{
    ProductClient.deleteProduct('asd').subscribe(deleted=>{
        expect(deleted).toBe(true);
        done();
    });
});