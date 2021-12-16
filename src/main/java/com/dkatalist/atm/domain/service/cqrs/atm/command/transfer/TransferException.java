package com.dkatalist.atm.domain.service.cqrs.atm.command.transfer;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.service.ServiceException;

public class TransferException extends ServiceException {
    private final String source;
    private final String recipient;
    private final int amount;

    protected TransferException(String errorCode, String source, String recipient, int amount, String message) {
        super(errorCode, message);
        this.source = source;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public String getRecipient() {
        return recipient;
    }

    public int getAmount() {
        return amount;
    }

    public static TransferException amountMustGreaterThanOrEqualsTo(String source, String recipient, int amount) {
        validate(source, recipient);
        return new TransferException("01", source, recipient, amount,
                String.format("your transfer must be greater than or equals to %d", amount));
    }

    public static TransferException cannotTransferToSameAccount(String source, String recipient, int amount) {
        validate(source, recipient);
        return new TransferException("02", source, recipient, amount, "you can not transfer into same account");
    }

    private static void validate(String source, String recipient) {
        Guard.validateArgNotNullOrEmpty(source, "source");
        Guard.validateArgNotNullOrEmpty(recipient, "recipient");
    }
}