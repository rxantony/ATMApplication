package com.dkatalist.atm.domain.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AccountDb implements Db<String,Account> {
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

    Account iacc = oacc.get();
    iacc.setSaving(account.getSaving());
    return true;
  }

  @Override
  public boolean delete(String accountName) {
    Optional<Account> oacc = accounts.stream().filter(a-> a.getName().equals(accountName)).findFirst();
    if(oacc.isPresent())
      return false;
      
    accounts.remove(oacc.get());
    return true;
  }

  @Override
  public List<Account> where(Predicate<Account> filter) {
    return accounts.stream().filter(filter).map(Account::new).collect(Collectors.toList());
  }

  @Override
  public Optional<Account> first(Predicate<Account> filter) {
    return accounts.stream().filter(filter).findFirst();
  }

  @Override
  public boolean exists(Predicate<Account> filter) {
    return accounts.stream().filter(filter).count() > 0;
  }
}