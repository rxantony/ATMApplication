package com.dkatalist.atm.domain.service.atm.command.oweCalcullation;

import java.util.List;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.Request;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;

public class OweCallculationRequest implements Request<Integer> {

    private final Account account;
    private final Account recipient;
    private final int amount;
    private final List<Owe> oweList;

    public OweCallculationRequest(Account account, Account recipient, int amount, List<Owe> oweList) {
        Guard.validateArgNotNull(account, "account");
        Guard.validateArgNotNull(recipient, "recipient");
        Guard.validateArgMustBeGreaterThan(amount, 0, "amount");
        Guard.validateArgNotNull(oweList, "oweList");

        this.account = account;
        this.recipient = recipient;
        this.amount = amount;
        this.oweList = oweList;
    }

    public Account getAccount() {
        return account;
    }

    public Account getRecipient() {
        return recipient;
    }

    public int getAmount() {
        return amount;
    }

    public List<Owe> getOweList() {
        return oweList;
    }
}
