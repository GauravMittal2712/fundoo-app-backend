package com.bridgelabz.fundoo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse <T> {

    private int statusCode;
    private String message;
    private T data;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> APIResponse<T> success (
            int statusCode,
            String message,
            T data
    ) {
        return new APIResponse<>(statusCode, message, data, LocalDateTime.now());
    }

    public static <T> APIResponse<T> success (
            int statusCode,
            String message
    ) {
        return new APIResponse<>(statusCode, message, null, LocalDateTime.now());
    }
}
