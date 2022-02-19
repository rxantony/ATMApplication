package com.bank.atm.domain.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryDefault implements AccountRepository {
    private List<Account> db = new ArrayList<>();

    @Override
    public Optional<Account> get(String accountName) {
        var oacc = db.stream().filter(a -> a.getName().equals(accountName)).findFirst();
        if (!oacc.isPresent())
            return oacc;
        return Optional.of(new Account(oacc.get()));
    }

    @Override
    public void add(Account... accounts) {
        for (var account : accounts) {
            db.add(new Account(account));
        }
    }

    @Override
    public void update(Account... accounts) {
        for (var account : accounts) {
            var oacc = db.stream().filter(a -> a.getName().equals(account.getName())).findFirst();
            if (!oacc.isPresent())
                continue;
            Account acc = oacc.get();
            acc.setBalance(account.getBalance());
        }
    }

}
