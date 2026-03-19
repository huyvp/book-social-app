package com.profile.dto.response;

import static com.profile.constant.Constants.Pattern.TIME;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
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
