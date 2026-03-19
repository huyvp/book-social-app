package com.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateConversationRequest {
    @Pattern(regexp = "GROUP|DIRECT", message = "type must be specific values")
    String type;
    @Size(min = 1)
    @NotNull
    List<String> participantIds;
}
