import moment from "moment";

 /**
  * General purpose functions
  */
 export class Utils{

    public static clone<T>(obj: T):T{
        return JSON.parse(JSON.stringify(obj));
    }

    /**
     * It throws error if the object is undefined
     * It creates the error with undefinedErrorMsg as its error message
     */
    public static checkIfParameterIsUndefined<T>(parameter:T, undefinedErrorMsg: string):T{
        if(parameter===undefined){
            throw new Error(undefinedErrorMsg);
        }
        else{
            return parameter;
        }
    }

    public static getNLastChars(bigString: string, n: number){
        return bigString.substring(bigString.length-n,n);
    }

    public static dateToString(date: Date){
        return moment(date).format('DD-MM-YYYY');
    }

 }