import httpClient from '../configurations/httpClient';
import { API } from '../configurations/configuration';
import { getToken } from './localStorageService';

export const getMyPosts = async (page) => {
  return await httpClient.get(API.MY_POST, {
    headers: {
      Authorization: `Bearer ${getToken()}`
    },
    params: {
      // Backend (Spring) uses 0-based pagination, while our React UI uses 1-based pages
      page: page > 0 ? page - 1 : 0,
      size: 10
    }
  });
};

export const createPost = async (content) => {
  return await httpClient.post(
    API.CREATE_POST,
    { content: content },
    {
      headers: {
        Authorization: `Bearer ${getToken()}`,
        'Content-Type': 'application/json'
      }
    }
  );
};
