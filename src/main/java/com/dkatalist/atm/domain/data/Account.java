package com.dkatalist.atm.domain.data;

import com.dkatalist.atm.domain.common.Guard;

public class Account {
    private String name;
    private int saving;

    public Account(String name, int savingAmount){
        Guard.validateArgNotNullOrEmpty(name, "name");
        this.name = name;
        setSaving(savingAmount);
    }

    public Account(Account account){
        this.name = account.name;
        this.saving = account.saving;
    }

    public String getName(){
        return name;
    }

    public int getSaving(){
        return saving;
    }

    public void setSaving(int amount){
        Guard.validateArgMustBeGreaterThan(amount, -1, "amount");
        saving = amount;
    }
}
