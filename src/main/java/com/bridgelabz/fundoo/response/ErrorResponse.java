package com.bridgelabz.fundoo.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

   private int statusCode;
   private String error;
   private String message;
   private String path;
   private LocalDateTime timestamp;


   private ErrorResponse (
           int statusCode,
           String error,
           String message,
           String path
   ) {
       this.statusCode = statusCode;
       this.error = error;
       this.message = message;
       this.path = path;
       this.timestamp = LocalDateTime.now();
   }

   public static ErrorResponse of (
           int statusCode,
           String error,
           String message,
           String path
   ) {
       return new ErrorResponse(statusCode,error,message,path);
   }
}
