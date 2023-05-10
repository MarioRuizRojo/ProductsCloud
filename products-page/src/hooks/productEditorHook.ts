import { useEffect, useState } from "react";
import { Observable, Subject, Subscription } from "rxjs";

import { Product } from "../models/Product";
import ProductEditorState from "./ProductEditorState";

const productEditorIni : ProductEditorState = {
    loading : true,
    editedProduct: new Product(undefined),
    formValid: false,
    editing: false,
    categories: []
};
export let $productEditor: Subject<ProductEditorState> = new Subject<ProductEditorState>();

export function useProductEditorState(
    productEditorObservable: Observable<ProductEditorState>
    ): ProductEditorState{
  const [productEditor, setProductEditor] = useState(productEditorIni);

  useEffect(() => {
    const productEditorSubscription : Subscription = productEditorObservable.subscribe(setProductEditor);
    return () => productEditorSubscription.unsubscribe();
  }, [productEditorObservable]);

  return productEditor;
}