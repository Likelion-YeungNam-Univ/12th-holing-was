package com.example.holing.bounded_context.medicine.exception;

import com.example.holing.base.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum MedicineExceptionCode implements ExceptionCode {
    MEDICINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 영양제가 존재하지 않습니다."),
    ACCESS_DENIED_TO_MEDICINE(HttpStatus.FORBIDDEN, "해당 영양제에 접근 권한이 없습니다.");

    HttpStatus httpStatus;
    String cause;

    MedicineExceptionCode(HttpStatus httpStatus, String cause) {
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
