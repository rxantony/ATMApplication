package com.bank.atm.domain.exception;

import com.bank.atm.domain.common.ATMException;
import lombok.Getter;

@Getter
public class AccountNotExistsException extends ATMException {
    private final String accountName;

    public AccountNotExistsException(String accountName, String message) {
        super(message);
        this.accountName = accountName;
    }

    public static AccountNotExistsException create(String accountName) {
        var message = String.format("account with name %s is not exists", accountName);
        return new AccountNotExistsException(accountName, message);
    }

    public static AccountNotExistsException create(String accountName, String message) {
        return new AccountNotExistsException(accountName, message);
    }
}
