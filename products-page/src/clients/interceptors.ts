import { AxiosResponse } from "axios";
import customHistory from "../pages/customHistory";

export const intercept_ok = function(response: AxiosResponse<any, any>) {
    return response;
}

export const intercept_error = function (error:any) {    
    customHistory.replace('/notfound');
    return Promise.reject(error);
}