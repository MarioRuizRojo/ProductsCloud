/**
 * Product's category
 */
export class Category{
    /**
     * Category's identifier
     */
    private id : string;
    /**
     * Category's name
     */
    private name : string;

    /**
     * Constructor from jsonObject
     * @param category jsonObject
     */
    constructor(category : any){
        if(category===undefined){
            this.id='';
            this.name='';
        }
        else{
            this.id = category.id;
            this.name = category.name;
        }        
    }

    /**
     * 
     * @returns category's identifier
     */
    public getId(): string {
        return this.id;
    }
    /**
     * 
     * @param value to set category's identifier
     */
    public setId(value: string) {
        this.id = value;
    }

    /**
     * 
     * @returns category's name
     */
    public getName(): string {
        return this.name;
    }
    /**
     * 
     * @param value to set category's name
     */
    public setName(value: string) {
        this.name = value;
    }
}