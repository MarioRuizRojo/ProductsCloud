
import { useEffect, useRef } from "react";
import { ProductItem } from "../../components/productItem";
import { $productEditor, useProductEditorState } from "../../hooks/productEditorHook";
import { $products, useProductsState } from "../../hooks/productsHook";
import { Product } from "../../models/Product";
import ProductService from "../../services/ProductService";
import ProductEditorState from "../../hooks/ProductEditorState";

export function ProductListView(){
    const productEditorState: ProductEditorState = useProductEditorState($productEditor);
    const productsState = useProductsState($products);
    const firstRendered = useRef(true);
    useEffect(()=>{
        if(firstRendered.current){
            firstRendered.current=false;
            ProductService.getAllProducts().subscribe();
        }
    },[firstRendered]);
    return (
        <div className='card'>
            <div className='card-body'>
                <h5 className='title'>Product List</h5>
                <table className='table'>
                <tbody data-testid='idTableProducts'>
                    {productsState.map((productI : Product, index : number) =>
                        <ProductItem key={index} product={productI} index={index} onEdit={() => {
                            ProductService.editSelectedProduct(productI, productEditorState);
                        }}/>
                    )}
                </tbody>
                </table>
            </div>
        </div>
    );
}