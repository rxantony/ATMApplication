package com.dkatalist.atm.domain.service;

import com.dkatalist.atm.domain.common.Guard;

public class AccountNotExistsException extends ServiceException {
    private final String accountName;

    private AccountNotExistsException(String accountName, String message) {
        super("01", message);
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public static AccountNotExistsException create(String accountName) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        var message = String.format("account with name %s is not exists", accountName);
        return new AccountNotExistsException(accountName, message);
    }

    public static AccountNotExistsException create(String accountName, String message) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        return new AccountNotExistsException(accountName, message);
    }
}
