import {FaPenSquare, FaTrash} from 'react-icons/fa';

import { Utils } from "../Utils";
import { Product } from "../models/Product";
import ProductService from "../services/ProductService";

export type ProductItemProps = {
    product:Product;
    index:number;
    onEdit: ()=>void;
};

export function ProductItem({product, index, onEdit} : ProductItemProps){
    return (
        <tr key={'key'+index}>
            <td>{Utils.getNLastChars(product.getId(),4)}</td>
            <td>{product.getName()}</td>
            <td>{product.getPrice().toString()}</td>
            <td>{Utils.dateToString(product.getCreatedAt())}</td>
            <td>{product.getCategoryDTO().getName()}</td>
            <td>{product.getPicture()}</td>
            <td>
                <FaPenSquare className='text-info' 
                            data-testid={'edit' + index} onClick={onEdit}/>
                <FaTrash className='text-danger' 
                            data-testid={'delete' + index} onClick={() => {
                            ProductService.deleteSelectedProduct(product).subscribe();
                        }}/>                                    
            </td>
        </tr>
    );
}