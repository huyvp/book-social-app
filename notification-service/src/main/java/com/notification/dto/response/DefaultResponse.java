package com.notification.dto.response;

import static com.notification.constant.Constants.Pattern.TIME;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
