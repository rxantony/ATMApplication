package com.bank.atm.domain.data;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> get(String accountName);

    void add(Account... accounts);

    void update(Account... accounts);
}
