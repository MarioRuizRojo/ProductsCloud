/**
 * Default class to manage the data inside react-select component
 */
export class Option{

    /**
     * Option's identifier to distint from others in the selector
     */
    public value : string;
    /**
     * Option's label to show in the selector
     */
    public label : string;

    /**
     * Constructor from jsonObject
     * @param option jsonObject
     */
    constructor(option:any){
        if(option===undefined){
            this.value = '0';
            this.label = '';
        }
        else{
            this.value = option.value;
            this.label = option.label;
        }        
    }

    /**
     * 
     * @returns option's identifier
     */
    public getValue():string{
        return this.value;
    }
    /**
     * 
     * @returns option's caption to show
     */
    public getLabel():string{
        return this.label;
    }
}
