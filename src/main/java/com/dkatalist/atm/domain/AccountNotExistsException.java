package com.dkatalist.atm.domain;

public class AccountNotExistsException extends SessionBaseException {
    private AccountNotExistsException(String accountName) {
        super(accountName, String.format("account with name %s is not exists", accountName));
    }

    public static AccountNotExistsException create(String accountName){
        Guard.validateArgNotNull(accountName, "accountName");
        return new AccountNotExistsException(accountName);
    }
}
