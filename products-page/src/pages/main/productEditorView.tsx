import { useEffect, useRef, useState } from "react";
import Select, { ActionMeta, SingleValue } from 'react-select';

import { InputField } from "../../components/inputField";
import ProductService from "../../services/ProductService";
import { Option } from "../../models/Option";
import { OPTION_DEFAULT } from "../../constants";
import { $productEditor, useProductEditorState } from "../../hooks/productEditorHook";
import { Category } from "../../models/Category";
import ProductEditorState from "../../hooks/ProductEditorState";

export function ProductEditorView(){
    const [categoryOptions, setCategoryOptions] = useState<Option[]>([]);
    const [categoryOptionSelected, setCategoryOptionSelected] = useState<Option>(OPTION_DEFAULT);
    const productEditorState: ProductEditorState = useProductEditorState($productEditor);
    const firstRendered = useRef(true);
    useEffect(()=>{
        if(firstRendered.current){
            firstRendered.current=false;
            ProductService.getAllCategoryOptions(productEditorState).subscribe((categoryOpts:Option[])=>{
                setCategoryOptions(categoryOpts);             
            });            
        }
    },[firstRendered, productEditorState]);
    useEffect(()=>{
        const category : Category = productEditorState.editedProduct.getCategoryDTO();
        setCategoryOptionSelected(ProductService.getOptionCategory(categoryOptions,category));  
    },[productEditorState.editedProduct, categoryOptions]);
    return(
        <div className="card">
            <div className="card-body">
                <h5 className="title mb-3">
                    <span>Product Editor</span>
                    {productEditorState.loading && 
                        <div className="spinner-border float-end" role="status">
                            <span className="visually-hidden">loading...</span>
                        </div>
                    }
                </h5>
                <form>
                    <InputField inputType='text' inputId='Name' inputLabel='Name' 
                        inputValue={productEditorState.editedProduct.getName()}
                        onChange={e=>ProductService.changeInputField(e,'idName',productEditorState)}/>
                    <InputField inputType='number' inputId='Price' inputLabel='Price' 
                        inputValue={productEditorState.editedProduct.getPrice()}
                        onChange={e=>ProductService.changeInputField(e,'idPrice',productEditorState)}/>
                    <div className='form-group'>
                        <label htmlFor='nameCategorySelector'>Category</label>
                        <Select options={categoryOptions} value={categoryOptionSelected}
                            className='form-control' id='idCategorySelector' name='nameCategorySelector' 
                            aria-label='idCategorySelector'
                            onChange={(option:SingleValue<Option>,action:ActionMeta<Option>)=>
                                ProductService.selectCategory(productEditorState,option)
                            }/>
                    </div>                                        
                    <div className='d-grid gap-2'>
                        <button className='btn btn-success btn-lg' type='button' 
                            data-testid='idProductEditorButton' id='idProductEditorButton' 
                            onClick={()=>ProductService.addOrUpdateProductAndRefresh(productEditorState).subscribe()}
                            disabled={!productEditorState.formValid}>
                                <i className='fas fa-database' />
                                {productEditorState.editing ? 'savechanges': 'add'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}