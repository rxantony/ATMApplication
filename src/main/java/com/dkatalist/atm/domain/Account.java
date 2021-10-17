package com.dkatalist.atm.domain;

public class Account {
    private String name;
    private int saving;

    public Account(String name, int savingAmount){
        setName(name);
        setSaving(savingAmount);
    }

    public Account(Account account){
        this.name = account.getName();
        this.saving = account.getSaving();
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        Guard.validateArgNotNullOrEmpty(name, "name");
        this.name = name;
    }

    public int getSaving(){
        return saving;
    }

    public void setSaving(int amount){
        Guard.validateArgMustBeGreaterThan(amount, -1, "amount");
        saving = amount;
    }
}
