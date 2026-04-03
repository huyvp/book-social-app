import httpClient from '../configurations/httpClient';
import { API } from '../configurations/configuration';

export const getMyInfo = async () => {
  return await httpClient.get(API.MY_INFO);
};

export const updateProfile = async (profileData) => {
  return await httpClient.put(API.UPDATE_PROFILE, profileData);
};

export const uploadAvatar = async (formData) => {
  return await httpClient.put(API.UPDATE_AVATAR, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
};

export const search = async (keyword) => {
  return await httpClient.get(API.SEARCH_USER, {
    params: { keyword: keyword }
  });
};
