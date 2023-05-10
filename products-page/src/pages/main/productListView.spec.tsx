import { Observable, of } from "rxjs";
import ProductService from "../../services/ProductService";
import { RenderResult, render, waitFor } from "@testing-library/react";
import { ProductListView } from "./productListView";
import { threeProducts, waitMicroSeconds } from "../../fixtures/fixtures";
import { $products } from "../../hooks/productsHook";

jest.mock('../../services/ProductService');

let renderMocked: RenderResult;
let mockGetAllProducts : jest.Mock;

beforeEach(function():void{
    mockGetAllProducts = jest.fn(function():Observable<void>{return of()});  

    ProductService.getAllProducts = mockGetAllProducts;   
    renderMocked = render(
        <ProductListView/>
    );
});

it('getAllProducts ok', function():void{    
    expect(mockGetAllProducts).toHaveBeenCalled();   
});

it('threeProducts ok', function(done):void{    
    $products.subscribe(async function():Promise<void>{
        await waitMicroSeconds(50);
        await waitFor(async () => {
            expect(renderMocked).toMatchSnapshot('threeProducts');
        });
        done();
    });
    $products.next(threeProducts);      
});