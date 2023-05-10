import {fireEvent, render, RenderResult} from '@testing-library/react';
import { ProductItem } from "./productItem";
import { product01, waitMicroSeconds } from '../fixtures/fixtures';
import ProductService from '../services/ProductService';
import { Observable, of, Subject } from 'rxjs';
import { Product } from '../models/Product';

jest.mock('../services/ProductService');
let renderMocked:RenderResult;
let mockOnEdit : jest.Mock;
const $deleteListener: Subject<Product> = new Subject<Product>();
function onDelete(prod:Product) : Observable<void>{
    $deleteListener.next(prod);
    return of();
}

beforeAll(function():void{    
    mockOnEdit = jest.fn(()=>{});
    ProductService.deleteSelectedProduct = onDelete;    
});

beforeEach(function():void{
    renderMocked = render(
        <table>
            <tbody>
                <ProductItem product={product01} index={0} onEdit={mockOnEdit}/>
            </tbody>
        </table>
    );
});

it('check constructor product details inside ProductItem', function():void{
    expect(renderMocked).toMatchSnapshot();
});

it('onEdit if pencil clicked', async function():Promise<void>{
    let editButton : HTMLElement =  renderMocked.getByTestId('edit0');
    fireEvent.click(editButton);
    await waitMicroSeconds(50);
    expect(mockOnEdit).toHaveBeenCalled();
});

it('delete if trash clicked', function(done):void{
    let deleteButton : HTMLElement =  renderMocked.getByTestId('delete0');
    $deleteListener.subscribe(product=>{
        expect(product.getId()).toBe(product01.getId());
        done();
    });
    fireEvent.click(deleteButton);
});
