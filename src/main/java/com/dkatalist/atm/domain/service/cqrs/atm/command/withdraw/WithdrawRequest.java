package com.dkatalist.atm.domain.service.cqrs.atm.command.withdraw;

import com.dkatalist.atm.domain.common.cqrs.handler.RequestWithException;
import com.dkatalist.atm.domain.service.ServiceException;

public class WithdrawRequest implements RequestWithException<WithdrawResult, ServiceException> {
    private int amount;
    private String accountName;

    public WithdrawRequest(String accountName, int amount) {
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
