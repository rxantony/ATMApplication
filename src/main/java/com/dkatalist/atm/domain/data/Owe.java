package com.dkatalist.atm.domain.data;

import java.util.Date;
import com.dkatalist.atm.domain.common.Guard;

public class Owe{
    private String account1;
    private String account2;
    private int amount;

    private Date createdAt;

    public Owe(String account1, String account2, int amount){
        Guard.validateArgNotNullOrEmpty(account1, "account1");
        Guard.validateArgNotNullOrEmpty(account2, "account2");
        
        this.account1 = account1;
        this.account2 = account2;
        setAmount(amount);
        this.createdAt = new Date();
    }

    public Owe(Owe owed){
        this.account1 = owed.account1;
        this.account2 = owed.account2;
        this.amount = owed.amount;
    }
    
    public String getAccount1(){
        return account1;
    }

    public String getAccount2(){
        return account2;
    }

    public int getAmount(){
        return amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public Date createdAt(){
        return createdAt;
    }
}
