package com.dkatalist.atm.domain.service.cqrs.atm.query.getOweList;

import java.util.List;

import com.dkatalist.atm.domain.common.cqrs.handler.Request;
import com.dkatalist.atm.domain.data.Owe;

public class GetOweListRequest implements Request<List<Owe>> {
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
