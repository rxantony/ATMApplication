package com.bank.atm.domain.service;

import com.bank.atm.domain.common.ATMException;

public class ServiceException extends ATMException {
    
    private final String errorCode;

    protected ServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected ServiceException(String errorCode, Exception inner) {
        super(inner);
        this.errorCode = errorCode;
    }

    protected ServiceException(String errorCode, String message, Exception inner) {
        super(message, inner);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
