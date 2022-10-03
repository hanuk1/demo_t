package com.example.demo.exception;

import com.example.demo.beans.AppError;

public class ApplicationException extends RuntimeException {

    AppError appError;

    public ApplicationException(AppError appError) {
        this.appError = appError;
    }

    public AppError getAppError() {
        return appError;
    }
}
