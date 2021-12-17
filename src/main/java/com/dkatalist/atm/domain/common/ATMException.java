package com.dkatalist.atm.domain.common;

public class ATMException extends Exception {
    
    protected ATMException(String message) {
        super(message);
    }

    protected ATMException(Exception inner) {
        super(inner);
    }

    protected ATMException(String message, Exception inner) {
        super(message, inner);
    }
}
