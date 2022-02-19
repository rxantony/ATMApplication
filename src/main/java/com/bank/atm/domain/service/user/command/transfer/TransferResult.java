package com.bank.atm.domain.service.user.command.transfer;

import java.util.Collections;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.data.Owe;
import com.bank.atm.domain.service.OweListResult;
import com.bank.atm.domain.service.TransactionResult;

public class TransferResult extends TransactionResult implements OweListResult {
    private final String recipient;
    private final Iterable<Owe> oweList;
    private static final Iterable<Owe> EMPTY_OWES = Collections.emptyList();
    public TransferResult(String accountName, String recipient) {
        this(accountName, recipient, EMPTY_OWES);
    }

    /*public TransferResult(String accountName, String recipient, int amount, int balace) {
        this(accountName, recipient, amount, balace, new ArrayList<Owe>());
    }*/

    public TransferResult(String accountName, String recipient, Iterable<Owe> owes) {
        this(accountName, recipient, 0, 0, EMPTY_OWES);
    }

    public TransferResult(String accountName, String recipient, int amount, int balace, Iterable<Owe> owes) {
        super(accountName, amount, balace);
        Guard.validateArgNotNullOrEmpty(recipient, "recipient");
        Guard.validateArgNotNull(owes, "owes");
        this.recipient = recipient;
        this.oweList = owes;
    }

    public String getRecipient() {
        return recipient;
    }

    public Iterable<Owe> getOwes() {
        return oweList;
    }
}
