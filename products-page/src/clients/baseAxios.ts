import axios, { AxiosInstance } from 'axios';
import { intercept_error, intercept_ok } from './interceptors';

const baseClient: AxiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_URL
});

baseClient.interceptors.response.use(intercept_ok, intercept_error);

export default baseClient;