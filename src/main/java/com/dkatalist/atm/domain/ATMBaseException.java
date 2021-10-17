package com.dkatalist.atm.domain;

public class ATMBaseException extends Exception {
    private final String errorCode;

    protected ATMBaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
