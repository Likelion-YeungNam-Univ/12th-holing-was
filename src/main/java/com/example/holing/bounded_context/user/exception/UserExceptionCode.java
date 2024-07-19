package com.example.holing.bounded_context.user.exception;

import com.example.holing.base.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum UserExceptionCode implements ExceptionCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 조회할 수 없습니다."),
    MATE_NOT_FOUND(HttpStatus.NOT_FOUND, "짝꿍을 조회할 수 없습니다."),
    USER_MATE_EXISTS(HttpStatus.CONFLICT, "사용자의 짝꿍이 존재합니다."),
    TARGET_MATE_EXISTS(HttpStatus.CONFLICT, "대상자의 짝꿍이 존재합니다.");

    HttpStatus httpStatus;
    String cause;

    UserExceptionCode(HttpStatus httpStatus, String cause) {
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
