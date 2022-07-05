package com.bank.atm.domain.service.account.command.createaccount;

import com.bank.atm.domain.common.Guard;

public class CreateAccountResult {
    private final String name;
    private int balance;

    public CreateAccountResult(String name, int balace) {
        Guard.validateArgNotNullOrEmpty(name, "name");
        this.name = name;
        this.balance = balace;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
}
