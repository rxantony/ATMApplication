package com.bank.atm.domain.service.account.query.getAccount;

import java.util.Optional;

import com.bank.atm.domain.common.handler.Request;
import com.bank.atm.domain.data.Account;

public class GetAccountRequest implements Request<Optional<Account>> {
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
