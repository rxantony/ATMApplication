package com.dkatalist.atm.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountMemoryDb implements AccountDb {
  private List<Account> accounts = new ArrayList<>();

  @Override
  public Optional<Account> get(String accountName) {
    Optional<Account> oacc = accounts.stream().filter(a -> a.getName().equals(accountName)).findFirst();
    if (!oacc.isPresent())
      return oacc;
    return Optional.of(new Account(oacc.get()));
  }

  @Override
  public void add(Account account) {
    accounts.add(new Account(account));
  }

  @Override
  public boolean update(Account account) {
    Optional<Account> oacc = accounts.stream().filter(a -> a.getName().equals(account.getName())).findFirst();
    if (!oacc.isPresent())
      return false;

    Account acc = oacc.get();
    acc.setSaving(account.getSaving());
    return true;
  }
}