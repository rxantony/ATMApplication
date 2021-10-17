package com.dkatalist.atm.domain;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("session is expired");
    }
}
