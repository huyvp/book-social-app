package com.file.dto.response;

import static com.file.constant.Constants.Pattern.TIME;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


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
