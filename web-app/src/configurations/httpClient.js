import axios from 'axios';
import { CONFIG } from './configuration';
import { getToken } from '../services/localStorageService';

const httpClient = axios.create({
  baseURL: CONFIG.API_GATEWAY,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
});

httpClient.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

httpClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // You can also call a method from authenticationService here to clear token and redirect,
      // but to avoid circular dependencies, it might be easier to just remove it or redirect directly.
      // import { removeToken } from '../services/localStorageService';
      // removeToken();
      // window.location.href = '/login'; // if required
    }
    return Promise.reject(error);
  }
);

export default httpClient;
