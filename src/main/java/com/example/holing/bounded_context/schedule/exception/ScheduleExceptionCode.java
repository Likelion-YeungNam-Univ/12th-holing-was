package com.example.holing.bounded_context.schedule.exception;

import com.example.holing.base.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum ScheduleExceptionCode implements ExceptionCode {
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    INVALID_DATETIME(HttpStatus.BAD_REQUEST, "잘못된 일정입니다.");
    
    HttpStatus httpStatus;
    String cause;

    ScheduleExceptionCode(HttpStatus httpStatus, String cause) {
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
