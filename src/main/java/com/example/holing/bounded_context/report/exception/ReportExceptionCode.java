package com.example.holing.bounded_context.report.exception;

import com.example.holing.base.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum ReportExceptionCode implements ExceptionCode {
    TEST_HISTORY_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "해당 증상 테스트 기록이 존재하지 않습니다."),
    ACCESS_DENIED_TO_TEST_HISTORY(HttpStatus.FORBIDDEN, "해당 증상 테스트 기록에 접근 권한이 없습니다."),
    TEST_HISTORY_NOT_FOUNT_BY_USER(HttpStatus.NOT_FOUND, "해당 유저의 증상 테스트 기록이 존재하지 않습니다."),
    USER_REPORT_PERIOD_NOT_MET(HttpStatus.BAD_REQUEST, "마지막 증상 테스트 이후 7일이 경과해야 합니다.");


    HttpStatus httpStatus;
    String cause;

    ReportExceptionCode(HttpStatus httpStatus, String cause) {
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
