package com.dkatalist.atm.domain.service.atm.command.withdraw;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.service.ServiceException;

public class WithdrawException extends ServiceException {
    private final String accountName;
    private final int amount;

    protected WithdrawException(String errorCode, String accountName, int amount, String message) {
        super(errorCode, message);
        this.accountName = accountName;
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAmount() {
        return amount;
    }

    public static WithdrawException notEnoughAmount(String accountName, int amount) {
        validate(accountName);
        return new WithdrawException("01", accountName, amount,
                String.format("your saving account is not sufficient for withdrawal transaction"));
    }

    public static WithdrawException amountMustGreaterThanOrEqualsTo(String accountName, int amount) {
        validate(accountName);
        return new WithdrawException("02", accountName, amount,
                String.format("your withdrawals must be greater than or equals to %d", amount));
    }

    private static void validate(String accountName) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
    }
}