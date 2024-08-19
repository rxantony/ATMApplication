package com.bank.atm.domain.data.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bank.atm.domain.data.model.Account;

public class DefaultAccountRepository implements AccountRepository {
	private final ArrayList<Account> db = new ArrayList<>();

	@Override
	public Optional<Account> get(String accountName) {
		return db.stream().filter(a -> a.getName().equals(accountName))
				.findFirst();
	}

	@Override
	public Account add(Account account) {
		return Optional.ofNullable(account)
				.map(a -> {
					var newAcc = new Account(account);
					db.add(newAcc);
					return newAcc;
				})
				.orElseThrow(() -> new IllegalArgumentException("account is required"));
	}

	@Override
	public Optional<Account> update(Account account) {
		return Optional.ofNullable(account)
				.flatMap(a -> get(a.getName())
						.map(ia -> {
							ia.setName(a.getName());
							ia.setBalance(a.getBalance());
							return ia;
						}));
	}

	@Override
	public Collection<Account> update(Collection<Account> accounts) {
		return Optional.ofNullable(accounts)
				.map(l -> l.stream()
						.map(a -> update(a).orElse(null))
						.collect(Collectors.toCollection(ArrayList::new)))
				.orElseThrow(() -> new IllegalArgumentException("accounts is required"));
	}
}
