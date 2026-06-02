package com.bridgelabz.fundoo.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@Builder
public class APIResponse <T> {

    private final LocalDateTime TIMESTAMP = LocalDateTime.now();

    private int statusCode;

    private HttpStatus status;

    private String message;

    private T data;


}
