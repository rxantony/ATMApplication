package com.dkatalist.atm.domain.service.account.command.createAccount;

import com.dkatalist.atm.domain.common.handler.Request;
import com.dkatalist.atm.domain.data.Account;

public class CreateAccountRequest implements Request<Account> {
    private int balance;
    private String accountName;

    public CreateAccountRequest(String accountName, int balance) {
        setAccountName(accountName);
        setBalance(balance);
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int amount) {
        balance = amount;
    }
}
