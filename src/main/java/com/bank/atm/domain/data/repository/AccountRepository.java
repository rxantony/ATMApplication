package com.bank.atm.domain.data.repository;

import java.util.Collection;
import java.util.Optional;

import com.bank.atm.domain.data.model.Account;

public interface AccountRepository {
    
    Optional<Account> get(String accountName);

    Account add(Account account);

    Optional<Account> update(Account account);
    
    Collection<Account> update(Collection<Account> accounts);
}
