package com.dkatalist.atm.domain.service.facade;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.service.ServiceException;

public class TransactionException extends ServiceException {
    
    private final String accountName;
    private final String transaction;
    private final int amount;

    protected TransactionException(String errorCode,  String accountName, String transaction, int amount, String message) {
        super(errorCode, message);
        this.accountName = accountName;
        this.transaction = transaction;
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAmount() {
        return amount;
    }

    public String getTransaction(){
        return transaction;
    }

    public static TransactionException notEnoughAmount(String accountName, String transaction, int amount) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNullOrEmpty(transaction, "transaction");
        return new TransactionException("01",accountName, transaction, amount, String.format("your saving account is not sufficient for %s transaction", transaction));
    }

    public static TransactionException amountMustGreaterThanOrEqualsTo(String accountName, String transaction, int amount) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNullOrEmpty(transaction, "transaction");
        return new TransactionException("02",accountName, transaction, amount, String.format("your %s must be greater than or equals to %d", transaction, amount));
    }
}
