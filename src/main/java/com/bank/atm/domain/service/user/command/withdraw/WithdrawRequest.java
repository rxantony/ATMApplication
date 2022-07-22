package com.bank.atm.domain.service.user.command.withdraw;

import com.bank.atm.domain.common.handler.RequestWithError;
import com.bank.atm.domain.service.ServiceException;

public class WithdrawRequest implements RequestWithError<WithdrawResult, ServiceException> {
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
