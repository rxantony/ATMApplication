package com.dkatalist.atm.domain.data;

import java.util.Date;
import com.dkatalist.atm.domain.common.Guard;

public class Owe {
    private final String account1;
    private final String account2;
    private final Date createdAt;
    private int amount;

    public Owe(String account1, String account2, int amount) {
        this(account1, account2, amount, new Date());
    }

    public Owe(String account1, String account2, int amount, Date createdAt) {
        Guard.validateArgNotNullOrEmpty(account1, "account1");
        Guard.validateArgNotNullOrEmpty(account2, "account2");

        this.account1 = account1;
        this.account2 = account2;
        setAmount(amount);
        this.createdAt = createdAt;
    }

    public Owe(Owe owe) {
        Guard.validateArgNotNull(owe, "owe");
        this.account1 = owe.account1;
        this.account2 = owe.account2;
        this.amount = owe.amount;
        this.createdAt = owe.createdAt;
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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
