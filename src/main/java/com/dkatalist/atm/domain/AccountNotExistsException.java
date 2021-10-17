package com.dkatalist.atm.domain;

public class AccountNotExistsException extends ATMBaseException {
    private final String accountName;

    private AccountNotExistsException(String accountName) {
        super("01", String.format("account with name %s is not exists", accountName));
        this.accountName = accountName;
    }

    public String getAccountName(){
        return accountName;
    }

    public static AccountNotExistsException create(String accountName){
        Guard.validateArgNotNull(accountName, "accountName");
        return new AccountNotExistsException(accountName);
    }
}
