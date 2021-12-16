package com.dkatalist.atm.domain.application.facade;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("session is expired");
    }
}
