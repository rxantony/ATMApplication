package com.bank.atm.domain.application;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("session is expired");
    }
}
