package com.example.atm.base.exception;

import java.time.LocalDateTime;

public record ExceptionResponse(
        LocalDateTime timeStamp,
        String name,
        String cause
) {
    public static ExceptionResponse fromEntity(ExceptionCode exceptionCode) {
        return new ExceptionResponse(
                LocalDateTime.now(),
                exceptionCode.getName(),
                exceptionCode.getCause()
        );
    }
}
