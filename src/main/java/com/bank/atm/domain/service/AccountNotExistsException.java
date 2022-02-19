package com.bank.atm.domain.service;

import com.bank.atm.domain.common.Guard;

public class AccountNotExistsException extends ServiceException {
    private final String accountName;

    protected AccountNotExistsException(String accountName, String message) {
        super("01", message);
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public static AccountNotExistsException create(String accountName) {
        validate(accountName);
        var message = String.format("account with name %s is not exists", accountName);
        return new AccountNotExistsException(accountName, message);
    }

    public static AccountNotExistsException create(String accountName, String message) {
        validate(accountName);
        return new AccountNotExistsException(accountName, message);
    }
    
    private static void validate(String accountName){
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
    }
}
