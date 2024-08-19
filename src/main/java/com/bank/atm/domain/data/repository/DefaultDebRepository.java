package com.bank.atm.domain.data.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bank.atm.domain.data.model.Debt;

public class DefaultDebRepository implements DebtRepository {
	private final ArrayList<Debt> db = new ArrayList<>();

	@Override
	public Debt add(Debt debt) {
		return Optional.ofNullable(debt)
				.map(o -> {
					var newDebt = new Debt(o);
					db.add(o);
					return newDebt;
				})
				.orElseThrow(() -> new IllegalArgumentException("debt is required"));
	}

	@Override
	public Collection<Debt> add(Collection<Debt> debts) {
		return Optional.ofNullable(debts)
				.map(l -> l.stream()
						.map(o -> add(o))
						.collect(Collectors.toCollection(ArrayList::new)))
				.orElseThrow(() -> new IllegalArgumentException("debts is required"));

	}

	@Override
	public Optional<Debt> update(Debt debt) {
		return Optional.ofNullable(debt)
			.flatMap(d -> get(d.getAccountName1(), d.getAccountName2()))
			.map(d -> {
				d.setAmount(debt.getAmount());
				return d;
			});
	}

	@Override
	public Collection<Debt> update(Collection<Debt> debts) {
		return Optional.ofNullable(debts)
				.map(l -> l.stream()
						.map(o -> update(o).orElse(null))
						.collect(Collectors.toCollection(ArrayList::new)))
				.orElseThrow(() -> new IllegalArgumentException("debts is required"));
	}

	@Override
	public Optional<Debt> get(String account1, String accoun2) {
		return db.stream().filter(o -> o.getAccountName1().equals(account1) && o.getAccountName2().equals(accoun2))
				.findFirst();
	}

	@Override
	public Optional<Debt> getDebtTo(String accountName1, String accountName2) {
		return db.stream().filter(
				o -> o.getAccountName1().equals(accountName1) && o.getAccountName2().equals(accountName2) && o.getAmount() < 0)
				.findFirst();
	}

	@Override
	public Optional<Debt> getDebtFrom(String accountName1, String accountName2) {
		return db.stream().filter(
				o -> o.getAccountName1().equals(accountName1) && o.getAccountName2().equals(accountName2) && o.getAmount() > 0)
				.findFirst();
	}

	@Override
	public List<Debt> getList(String accountName) {
		return db.stream().filter(o -> o.getAccountName1().equals(accountName))
				.sorted(Comparator.comparing(Debt::getCreatedAt).reversed())
				.map(Debt::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<Debt> getDebtToList(String accountName) {
		return db.stream()
				.filter(o -> o.getAccountName1().equals(accountName) && o.getAmount() < 0)
				.sorted(Comparator.comparing(Debt::getCreatedAt).reversed())
				.collect(Collectors.toList());
	}

	@Override
	public Collection<Debt> getDebtFromList(String accountName) {
		return db.stream()
				.filter(o -> o.getAccountName1().equals(accountName) && o.getAmount() > 0)
				.sorted(Comparator.comparing(Debt::getCreatedAt).reversed())
				.collect(Collectors.toList());
	}
}
