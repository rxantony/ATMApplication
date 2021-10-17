package com.dkatalist.atm.domain;

import java.util.Optional;

public interface AccountDb {
    Optional<Account> get(String accountName);
    void add(Account account);
    boolean update(Account account);
}
