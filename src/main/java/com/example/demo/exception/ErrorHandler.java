package com.example.demo.exception;

import com.example.demo.beans.AppError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = { ApplicationException.class})
    protected ResponseEntity<Object> handleApplicationError(
            ApplicationException ex, WebRequest request) {
        AppError appError = ex.getAppError();
        return handleExceptionInternal(ex, appError.getErrorMessage(),
                new HttpHeaders(), appError.getHttpStatus(), request);
    }

    @ExceptionHandler(value
            = { Exception.class})
    protected ResponseEntity<Object> handleApplicationError(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Unknown error occurred. Please reach out to support team",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
