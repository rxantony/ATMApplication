package com.dkatalist.atm.domain.application;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("session is expired");
    }
}
