package com.chat.mapper;

import com.chat.dto.request.MessageRequest;
import com.chat.dto.response.MessageResponse;
import com.chat.entity.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse toMessageResponse(Message message);

    Message toMessage(MessageRequest messageRequest);

    List<MessageResponse> toMessageResponse(List<Message> messages);
}
