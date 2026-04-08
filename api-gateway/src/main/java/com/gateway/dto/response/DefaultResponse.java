package com.gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponse<T> {
    @JsonFormat(pattern = "HH:mm:ss yyyy-MM-dd", timezone = "Asia/Bangkok")
    private String timestamp;
    private int code;
    private HttpStatus status;
    private String message;
    @Setter
    private Integer total;
    private T result;
}
