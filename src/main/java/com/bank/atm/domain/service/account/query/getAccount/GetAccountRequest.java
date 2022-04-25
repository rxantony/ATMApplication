package com.bank.atm.domain.service.account.query.getaccount;

import java.util.Optional;

import com.bank.atm.domain.common.handler.Request;

public class GetAccountRequest implements Request<Optional<GetAccountResult>> {
    private String accountName;
    public GetAccountRequest(String accountName) {
        setAccountName(accountName);
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
