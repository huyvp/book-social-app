package com.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.identity.constant.Constants.Pattern.TIME;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponse<T> {
    @JsonFormat(pattern = TIME, timezone = "Asia/Bangkok")
    private LocalDateTime timestamp;
    private int code;
    private HttpStatus status;
    @Setter
    private String message;
    @Setter
    private Integer total;
    private T result;
}
