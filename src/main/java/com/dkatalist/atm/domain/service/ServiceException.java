package com.dkatalist.atm.domain.service;

public class ServiceException extends Exception {
    private final String errorCode;

    protected ServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
