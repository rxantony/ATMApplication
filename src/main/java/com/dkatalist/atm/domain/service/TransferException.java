package com.dkatalist.atm.domain.service;

import com.dkatalist.atm.domain.common.Guard;

public class TransferException extends TransactionException {
    private final String recipient;

    protected TransferException(String errorCode, String source, String recipient, int amount , String message) {
        super(errorCode, source, "transfer", amount, message);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public static TransferException cannotTransferToSameAccount(String source, String recipient, int amount) {
        Guard.validateArgNotNullOrEmpty(source, "source");
        Guard.validateArgNotNullOrEmpty(recipient, "recipient");
        return new TransferException("02", source, recipient, amount, "you can not transfer into same account");
    }
}
