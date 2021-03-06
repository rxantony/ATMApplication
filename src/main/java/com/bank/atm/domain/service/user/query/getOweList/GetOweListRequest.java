package com.bank.atm.domain.service.user.query.getowelist;

import java.util.List;

import com.bank.atm.domain.common.handler.Request;

public class GetOweListRequest implements Request<List<GetOweResult>> {
    private String accountName;

    public GetOweListRequest(String accountName) {
        setAccountName(accountName);
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
