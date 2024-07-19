package com.example.atm.base.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    public HttpStatus getHttpStatus();

    public String getCause();

    public String getName();
}
