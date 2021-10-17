package com.dkatalist.atm.domain;

public class TransactionException extends SessionBaseException {
    private final String errorCode;
    private final String transaction;
    private final int amount;

    protected TransactionException(String errorCode,  String accountName, String transaction, int amount, String message) {
        super(accountName, message);
        this.errorCode = errorCode;
        this.transaction = transaction;
        this.amount = amount;
    }

    public String getErrorCode() {
        return errorCode;
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
}
