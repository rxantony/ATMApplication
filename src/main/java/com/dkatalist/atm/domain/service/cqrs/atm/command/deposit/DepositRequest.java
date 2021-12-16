package com.dkatalist.atm.domain.service.cqrs.atm.command.deposit;

import com.dkatalist.atm.domain.common.cqrs.handler.RequestWithException;
import com.dkatalist.atm.domain.service.ServiceException;

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
