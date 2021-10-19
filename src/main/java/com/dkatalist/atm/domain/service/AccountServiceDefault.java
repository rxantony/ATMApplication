package com.dkatalist.atm.domain.service;

import java.util.Optional;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Db;

public class AccountServiceDefault implements AccountService {
    private Db<String, Account> db;

    public AccountServiceDefault(Db<String, Account> db){
        Guard.validateArgNotNull(db, "db");
        this.db = db;
    }

    @Override
    public Optional<Account> getAccount(String accountName) {
        Optional<Account> oacc = db.get(accountName);
        if(!oacc.isPresent()){
            Account newAcc = new Account(accountName, 0);
            db.add(newAcc);
            return Optional.of(newAcc);
        }
        return oacc;
    }

    @Override
    public void create(Account account) {
        Optional<Account> acc =  db.get(account.getName());
        if(acc.isPresent())
            return;
        db.add(account);
    }
    
}
