import httpClient from '../configurations/httpClient';
import {API} from '../configurations/configuration';

export const getMyConversations = async () => {
  return await httpClient.get(API.MY_CONVERSATIONS);
};

export const createConversation = async (data) => {
  return await httpClient.post(API.CHAT_CONVERSATION, {
    type: data.type,
    participantIds: data.participantIds
  });
};

export const createMessage = async (data) => {
  return await httpClient.post(API.CHAT_MESSAGE, {
    conversationId: data.conversationId,
    message: data.message
  });
};

export const getMessages = async (conversationId) => {
  return await httpClient.get(
    `${API.CHAT_MESSAGE}?conversationId=${conversationId}`
  );
};
