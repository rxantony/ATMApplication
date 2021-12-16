package com.dkatalist.atm.domain.service.cqrs.atm.command.transfer;

import java.util.ArrayList;
import java.util.List;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.cqrs.OweListResult;
import com.dkatalist.atm.domain.service.cqrs.TransactionResult;

public class TransferResult extends TransactionResult implements OweListResult {
    private final String recipient;
    private final List<Owe> oweList;

    public TransferResult(String accountName, String recipient) {
        this(accountName, recipient, 0, 0);
    }

    public TransferResult(String accountName, String recipient, int amount, int balace) {
        this(accountName, recipient, amount, balace, new ArrayList<Owe>());
    }

    public TransferResult(String accountName, String recipient, int amount, int balace, List<Owe> oweList) {
        super(accountName, amount, balace);
        Guard.validateArgNotNullOrEmpty(recipient, "recipient");
        Guard.validateArgNotNull(oweList, "oweList");
        this.recipient = recipient;
        this.oweList = oweList;
    }

    public String getRecipient() {
        return recipient;
    }

    public List<Owe> getOweList() {
        return oweList;
    }
}
