package com.bank.atm.domain.service.account.query.getaccount;


import com.bank.atm.domain.common.Guard;

public class GetAccountResult {
    private final String name;
    private final int balance;

    public GetAccountResult(String name, int balance) {
        Guard.validateArgNotNullOrEmpty(name, "name");
        this.name = name;
        this.balance =  balance;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
}
