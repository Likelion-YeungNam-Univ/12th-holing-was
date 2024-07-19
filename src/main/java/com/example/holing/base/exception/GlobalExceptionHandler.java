package com.example.holing.base.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> handler(GlobalException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        ExceptionResponse response = ExceptionResponse.fromEntity(exceptionCode);
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }
}
