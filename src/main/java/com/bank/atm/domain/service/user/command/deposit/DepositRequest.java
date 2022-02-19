package com.bank.atm.domain.service.user.command.deposit;

import com.bank.atm.domain.common.handler.RequestWithException;
import com.bank.atm.domain.service.ServiceException;

public class DepositRequest implements RequestWithException<DepositResult, ServiceException> {
    private int amount;
    private String accountName;

    public DepositRequest(String accountName, int amount) {
        setAccountName(accountName);
        setAmount(amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
