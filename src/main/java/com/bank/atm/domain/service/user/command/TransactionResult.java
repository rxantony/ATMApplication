package com.bank.atm.domain.service.user.command;

import com.bank.atm.domain.common.Guard;

public abstract class TransactionResult {
    private final String accountName;
    private int amount;
    private int balace;

    protected TransactionResult(String accountName) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        this.accountName = accountName;
    }

    protected TransactionResult(String accountName, int amount, int balace) {
        this(accountName);
        setAmount(amount);
        setBalance(balace);
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        Guard.validateArgMustBeGreaterThan(amount, -1, "amount");
        this.amount = amount;
    }

    public int getBalance() {
        return balace;
    }

    public void setBalance(int amount) {
        Guard.validateArgMustBeGreaterThan(amount, -1, "amount");
        this.balace = amount;
    }
}