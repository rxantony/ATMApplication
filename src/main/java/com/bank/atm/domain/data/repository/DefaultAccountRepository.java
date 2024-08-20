package com.bank.atm.domain.data.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.data.model.Account;
import com.bank.atm.domain.mapper.AccountMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultAccountRepository implements AccountRepository {
	@NotNull
	private final AccountMapper mapper;

	private ArrayList<Account> db = new ArrayList<>();

	@Override
	public Optional<Account> get(String accountName) {
		return db.stream().filter(a -> a.getName().equals(accountName))
				.findFirst()
				.map(mapper::toModel);
	}

	@Override
	public Account add(Account account) {
		return Optional.ofNullable(account)
				.map(a -> {
					var newAcc = new Account(account);
					newAcc.setCreatedAt(new Date());
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
