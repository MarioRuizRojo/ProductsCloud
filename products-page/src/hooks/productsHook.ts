import { useEffect, useState } from "react";
import { Observable, Subject, Subscription } from "rxjs";

import { Product } from "../models/Product";

const productsIni : Product[] = [];
export const $products: Subject<Product[]> = new Subject<Product[]>();

export function useProductsState(productsObservable: Observable<Product[]>): Product[] {
  const [products, setProducts] = useState(productsIni);

  useEffect(() => {
    const productsSubscription : Subscription = productsObservable.subscribe(setProducts);
    return () => productsSubscription.unsubscribe();
  }, [productsObservable]);

  return products;
}