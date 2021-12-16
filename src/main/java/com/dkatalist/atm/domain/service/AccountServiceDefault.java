package com.dkatalist.atm.domain.service;

import java.util.Optional;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;

public class AccountServiceDefault implements AccountService {
    private AccountRepository repo;

    public AccountServiceDefault(AccountRepository repo) {
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
    }

    @Override
    public Optional<Account> getAccount(String accountName) {
        var oacc = repo.get(accountName);
        if (!oacc.isPresent()) {
            var newAcc = new Account(accountName, 0);
            repo.add(newAcc);
            return Optional.of(newAcc);
        }
        return oacc;
    }

    @Override
    public boolean create(Account account) {
        var acc = repo.get(account.getName());
        if (acc.isPresent())
            return false;
        repo.add(account);
        return true;
    }

}
