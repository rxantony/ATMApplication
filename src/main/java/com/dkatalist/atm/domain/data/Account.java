package com.dkatalist.atm.domain.data;

import com.dkatalist.atm.domain.common.Guard;

public class Account {
    private final String name;
    private int balance;

    public Account(String name, int balace) {
        Guard.validateArgNotNullOrEmpty(name, "name");
        this.name = name;
        setBalance(balace);
    }

    public Account(Account account) {
        Guard.validateArgNotNull(account, "account");
        this.name = account.name;
        this.balance = account.balance;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int amount) {
        Guard.validateArgMustBeGreaterThan(amount, -1, "amount");
        balance = amount;
    }
}
