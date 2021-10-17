package com.dkatalist.atm.domain;

public class SessionBaseException extends Exception {
    private final String accountName;

    protected SessionBaseException(String accountName, String message) {
        super(message);
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }
}
