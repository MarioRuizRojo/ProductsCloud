import { RenderResult, act, fireEvent, render, waitFor } from "@testing-library/react";
import ProductService from "../../services/ProductService";
import { $productEditor } from "../../hooks/productEditorHook";
import { product01, twoOptionCategory, category01, optionCategory02 } from "../../fixtures/fixtures";
import ProductEditorState from "../../hooks/ProductEditorState";
import { ProductEditorView } from "./productEditorView";
import { waitMicroSeconds } from "../../fixtures/fixtures";
import { Observable, of } from "rxjs";
import { Option } from "../../models/Option";
import { Category } from "../../models/Category";
import { SingleValue } from "react-select";
import selectEvent from 'react-select-event';

jest.mock('../../services/ProductService');
let renderMocked:RenderResult;
let mockGetAllCategoryOptions : jest.Mock;
let mockGetOptionCategory : jest.Mock;
let mockSelectCategory : jest.Mock;
let mockAddOrUpdate : jest.Mock;

beforeEach(function():void{
    mockGetAllCategoryOptions = jest.fn(()=>{return of(twoOptionCategory);});
    mockSelectCategory = jest.fn((
        productEditorState: ProductEditorState, 
        optionSelected: SingleValue<Option>
        )=>{
        let newProductEditorState = new ProductEditorState(productEditorState);
        newProductEditorState.editedProduct.setCategoryDTO(category01);
        $productEditor.next(newProductEditorState);
    });
    mockGetOptionCategory = jest.fn(function(categoryOptions: Option[], category: Category):Option{
        return categoryOptions[1];
    });    
    mockAddOrUpdate = jest.fn(function():Observable<void>{
        return of();
    });     

    ProductService.addOrUpdateProductAndRefresh = mockAddOrUpdate;
    ProductService.getAllCategoryOptions = mockGetAllCategoryOptions;    
    ProductService.getOptionCategory = mockGetOptionCategory;   
    ProductService.selectCategory = mockSelectCategory;    

    renderMocked = render(
        <ProductEditorView/>
    );
});

it('check constructor with 1 product data, ok', function(done):void{    
    const subscription = $productEditor.subscribe(async function():Promise<void>{
        await waitMicroSeconds(50);
        await waitFor(() => {
            expect(renderMocked).toMatchSnapshot('oneProduct');
        });
        subscription.unsubscribe();
        done();
    });

    let newProductEditorState = new ProductEditorState(undefined);
    newProductEditorState.editedProduct=product01;
    newProductEditorState.loading=false;
    newProductEditorState.formValid=true;
    act(() => {
        $productEditor.next(newProductEditorState); 
    });       
});

it('getAllCategoryOptions first rendered, two categories, ok', function():void{    
    expect(renderMocked).toMatchSnapshot('twoCategories');
});

it('selectCategory & getOptionCategory, user choose one category, ok', async function():Promise<void>{
    const categorySelector : HTMLElement =  renderMocked.getByLabelText('idCategorySelector');    
    await selectEvent.select(categorySelector, optionCategory02.getLabel());    
    await waitFor(() => {
        expect(mockSelectCategory).toHaveBeenCalled();
        expect(mockGetOptionCategory).toHaveBeenCalled();
        expect(renderMocked).toMatchSnapshot('secondCategorySelected');
    });   
});

it('addOrUpdateProductAndRefresh, user clicks save ok',async function():Promise<void>{
    let newProductEditorState = new ProductEditorState(undefined);
    newProductEditorState.editedProduct=product01;
    newProductEditorState.loading=false;
    newProductEditorState.formValid=true;
    act(() => {
        $productEditor.next(newProductEditorState); 
    });   
    await waitMicroSeconds(50);
    const editorButton : HTMLElement =  renderMocked.getByTestId('idProductEditorButton');
    await act(async function():Promise<void> {
        fireEvent.click(editorButton);
    });
    await waitMicroSeconds(50);
    await waitFor(() => {
        expect(mockAddOrUpdate).toHaveBeenCalled();
        expect(renderMocked).toMatchSnapshot('addOrUpdate');
    });
});