package com.dkatalist.atm.domain.service.facade;

import java.util.Optional;

import com.dkatalist.atm.domain.data.Account;

public interface AccountService {

    Optional<Account> getAccount(String name);

    boolean create(Account account);
}
