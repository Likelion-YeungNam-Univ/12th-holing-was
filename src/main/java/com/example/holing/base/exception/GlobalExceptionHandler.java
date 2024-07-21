package com.example.holing.base.exception;

import com.example.holing.bounded_context.auth.exception.AuthExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> handler(GlobalException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        ExceptionResponse response = ExceptionResponse.fromEntity(exceptionCode);
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }

    //API 호출 관련 예외
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionResponse> handler(HttpClientErrorException e) {
        ExceptionResponse response = ExceptionResponse.fromEntity("API_ERROR", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //JWT 유효성 검사 관련 예외
    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<ExceptionResponse> handler(SignatureException e) {
        ExceptionResponse response = ExceptionResponse.fromEntity(AuthExceptionCode.INVALID_TOKEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<ExceptionResponse> handler(MalformedJwtException e) {
        ExceptionResponse response = ExceptionResponse.fromEntity(AuthExceptionCode.WRONG_TOKEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ExceptionResponse> handler(ExpiredJwtException e) {
        ExceptionResponse response = ExceptionResponse.fromEntity(AuthExceptionCode.EXPIRE_TOKEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    //SYSTEM
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> handler(Exception e) {
        ExceptionResponse response = ExceptionResponse.fromEntity("SYSTEM_ERROR", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
