package com.dkatalist.atm.domain.service.cqrs.atm.command.deposit;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.service.ServiceException;

public class DepositException extends ServiceException {
    private final String accountName;
    private final int amount;

    protected DepositException(String errorCode, String accountName, int amount, String message) {
        super(errorCode, message);
        this.accountName = accountName;
        this.amount = amount;
    }

    protected DepositException(String errorCode, String accountName, int amount, Exception inner) {
        super(errorCode, inner);
        this.accountName = accountName;
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAmount() {
        return amount;
    }

    public static DepositException fromException(String accountName, int amount, Exception inner) {
        validate(accountName);
        return new DepositException("03", accountName, amount, inner);
    }

    public static DepositException notEnoughAmount(String accountName, int amount) {
        validate(accountName);
        return new DepositException("01", accountName, amount,
                "your saving account is not sufficient for deposit transaction");
    }

    public static DepositException amountMustGreaterThanOrEqualsTo(String accountName, int amount) {
        validate(accountName);
        return new DepositException("02", accountName, amount,
                String.format("your deposit must be greater than or equals to %d", amount));
    }

    private static void validate(String accountName) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
    }
}
