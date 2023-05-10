import {Category} from './Category'

/**
 * Product
 */
export class Product{
    /**
     * Product's identifier
     */
    private id: string;   
    /**
     * Product's name
     */ 
    private name: string; 
    /**
     * Product's price
     */    
    private price: number;  
    /**
     * Creation date of product 
     */   
    private createdAt: Date;   
    /**
     * Product's category
     */  
    private categoryDTO: Category;  
    /**
     * Picture file name of product 
     */   
    private picture: string;    

    /**
     * Constructor from jsonObject
     * @param product jsonObject
     */
    constructor(product : any){
        if(product===undefined){
            this.id = '';
            this.name = '';
            this.price = 0.0;
            this.createdAt = new Date();
            this.categoryDTO = new Category(undefined);
            this.picture = '';
        }else{
            this.id = product.id;
            this.name = product.name;
            try{
                this.price = parseFloat(product.price);
            }
            catch(e){
                this.price = 0.0
            }
            this.createdAt = new Date();
            if(Array.isArray(product.createdAt) && product.createdAt.length>3){
                this.createdAt = new Date(product.createdAt[1]+'/'+product.createdAt[2]+'/'+product.createdAt[0]);
            }
            else{
                this.createdAt = new Date(product.createdAt);
            }   
            this.categoryDTO = new Category(product.categoryDTO);
            this.picture = product.picture;
        }        
    }

    /**
     * 
     * @returns identifier
     */
    public getId(): string {
        return this.id;
    }
    /**
     * 
     * @param value to set product's identifier
     */
    public setId(value: string) {
        this.id = value;
    }
    /**
     * 
     * @returns product's name
     */
    public getName(): string {
        return this.name;
    }
    /**
     * 
     * @param value to set product's name
     */
    public setName(value: string) {
        this.name = value;
    }
    /**
     * 
     * @returns product's price
     */
    public getPrice(): number {
        return this.price;
    }
    /**
     * 
     * @param value to set product's price
     */
    public setPrice(value: number) {
        this.price = value;
    }
    /**
     * 
     * @returns creation date of product
     */
    public getCreatedAt(): Date {
        return this.createdAt;
    }
    /**
     * 
     * @param value to set creation date of product
     */
    public setCreatedAt(value: Date) {
        this.createdAt = value;
    }
    /**
     * 
     * @returns category
     */
    public getCategoryDTO(): Category {
        return this.categoryDTO;
    }
    /**
     * 
     * @param value to set product's category
     */
    public setCategoryDTO(value: Category) {
        this.categoryDTO = value;
    }
    /**
     * 
     * @returns Picture file name of product
     */
    public getPicture(): string {
        return this.picture;
    }
    /**
     * 
     * @param value to set picture file name of product
     */
    public setPicture(value: string) {
        this.picture = value;
    }
}