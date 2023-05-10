import { RenderResult, act, fireEvent, render, waitFor } from '@testing-library/react';
import baseClient from '../clients/baseAxios';
import { ProductDashboard } from '../pages/main/productDashboard';
import { category02, optionCategory02, product01, responseEmpty, responseFull, responseWithProducts, waitMicroSeconds } from '../fixtures/fixtures';
import selectEvent from 'react-select-event';
import { Product } from '../models/Product';
import { API_PRODUCTS } from '../constants';

jest.mock('../clients/baseAxios');
const mockedBaseClient = jest.mocked(baseClient,true);
let renderMocked:RenderResult;
beforeEach(function():void{
    mockedBaseClient.post.mockResolvedValueOnce({status:200, data: responseWithProducts});
    mockedBaseClient.put.mockResolvedValueOnce({status:200, data: responseWithProducts});
    mockedBaseClient.delete.mockResolvedValueOnce({status:200, data: responseEmpty});
    mockedBaseClient.get.mockResolvedValue({status:200, data: responseFull});
    renderMocked = render(
        <ProductDashboard/>
    );    
});

it('product list ok',async function():Promise<void>{
    await waitMicroSeconds(50);
    await waitFor(async () => {
        expect(renderMocked).toMatchSnapshot('threeProducts3Categories');
    });    
});

it('save new product ok',async function():Promise<void>{
    const newName : string = 'new product';    

    await waitMicroSeconds(50);
    await act(async () => {        
        const priceInput : HTMLElement =  renderMocked.getByTestId('idPrice');
        fireEvent.change(priceInput, {target: {value: 2.3}});    
        const nameInput : HTMLElement =  renderMocked.getByTestId('idName');
        fireEvent.change(nameInput, {target: {value: newName}});        
        const categorySelector : HTMLElement =  renderMocked.getByLabelText('idCategorySelector');    
        await selectEvent.select(categorySelector, optionCategory02.getLabel());   
    });
    await waitMicroSeconds(50);
    await act(async () => {
        const editorButton : HTMLElement =  renderMocked.getByTestId('idProductEditorButton');
        fireEvent.click(editorButton);
    });
    await waitMicroSeconds(50);
    await waitFor(async () => {        
        expect(mockedBaseClient.post).toHaveBeenCalled();
        expect(renderMocked).toMatchSnapshot('saveNew');
    });    
});

it('save edited product ok',async function():Promise<void>{
    const editedName : string = 'edited product';

    await waitMicroSeconds(50);
    await act(async () => {
        const editButton : HTMLElement =  renderMocked.getByTestId('edit0');
        fireEvent.click(editButton);
    });
    await waitMicroSeconds(50);
    await act(async () => {        
        const nameInput : HTMLElement =  renderMocked.getByTestId('idName');
        fireEvent.change(nameInput, {target: {value: editedName}});
    });
    await waitMicroSeconds(50);
    await act(async () => {
        const editorButton : HTMLElement =  renderMocked.getByTestId('idProductEditorButton');
        fireEvent.click(editorButton); 
    });
    await waitMicroSeconds(50);
    await waitFor(async () => {
        expect(mockedBaseClient.put).toHaveBeenCalled();
        const prod : Product = new Product(product01);
        prod.setName(editedName)
        expect(mockedBaseClient.put).toHaveBeenCalledWith(API_PRODUCTS, prod);
        expect(renderMocked).toMatchSnapshot('saveEdited');
    });    
});

it('remove product ok',async function():Promise<void>{
    mockedBaseClient.get.mockResolvedValue({status:200, data: responseFull});
    await waitMicroSeconds(50);
    const deleteButton : HTMLElement =  renderMocked.getByTestId('delete2');
    fireEvent.click(deleteButton);
    await waitMicroSeconds(50);
    await waitFor(async () => {
        expect(mockedBaseClient.delete).toHaveBeenCalled();
        expect(mockedBaseClient.delete).toHaveBeenCalledWith(API_PRODUCTS+'id03');
        expect(renderMocked).toMatchSnapshot('removed');
    }); 
});