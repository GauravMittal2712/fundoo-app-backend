package com.bridgelabz.fundoo.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@lombok.AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ErrorResponse {

   private int statusCode;
   private String error;
   private String message;
   private String path;
   
   @Builder.Default
   private LocalDateTime timestamp = LocalDateTime.now();

   public static ErrorResponse of (
           int statusCode,
           String error,
           String message,
           String path
   ) {
       return new ErrorResponse(statusCode, error, message, path, LocalDateTime.now());
   }
}
