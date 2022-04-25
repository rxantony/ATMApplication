package com.bank.atm.domain.service.user.query.getowelist;

import java.util.Date;

import com.bank.atm.domain.common.Guard;

public class GetOweResult {
    private final String account1;
    private final String account2;
    private final Date createdAt;
    private final int amount;

    public GetOweResult(String account1, String account2, int amount, Date createdAt) {
        Guard.validateArgNotNullOrEmpty(account1, "account1");
        Guard.validateArgNotNullOrEmpty(account2, "account2");
        Guard.validateArgNotNull(createdAt, "createdAt");

        this.account1 = account1;
        this.account2 = account2;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getAccount1() {
        return account1;
    }

    public String getAccount2() {
        return account2;
    }

    public int getAmount() {
        return amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }   
}
