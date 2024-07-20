package com.example.holing.bounded_context.auth.exception;

import com.example.holing.base.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum AuthExceptionCode implements ExceptionCode {

    //Authorization
    EMPTY_AUTHORIZATION(HttpStatus.BAD_REQUEST, "인증 값이 비어있습니다."),
    MISMATCH_TOKEN_TYPE(HttpStatus.BAD_REQUEST, "Bearer 토큰이 아닙니다"),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰형식 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 서명의 토큰 입니다."),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");

    HttpStatus httpStatus;
    String cause;

    AuthExceptionCode(HttpStatus httpStatus, String cause) {
        this.httpStatus = httpStatus;
        this.cause = cause;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCause() {
        return cause;
    }

    @Override
    public String getName() {
        return name();
    }
}
