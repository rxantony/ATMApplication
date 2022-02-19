package com.bank.atm.domain.service.user.command.transfer;

import com.bank.atm.domain.common.handler.RequestWithException;
import com.bank.atm.domain.service.ServiceException;

public class TransferRequest implements RequestWithException<TransferResult, ServiceException> {
    private String accountName;
    private String recipient;
    private int amount;

    public TransferRequest(String accountName, String recipient, int amount) {
        setAccountName(accountName);
        setRecipient(recipient);
        setAmount(amount);
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
