package com.bank.atm.domain.data.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.data.model.Debt;
import com.bank.atm.domain.mapper.DebtMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultDebtRepository implements DebtRepository {
	@NotNull
	private final DebtMapper mapper;
	
	private ArrayList<Debt> db = new ArrayList<>();

	@Override
	public Debt add(Debt debt) {
		return Optional.ofNullable(debt)
				.map(o -> {
					var newDebt = new Debt(o);
					newDebt.setCreatedAt(new Date());
					db.add(newDebt);
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
		return getCursor(account1,accoun2).map(Debt::new);
	}

	private Optional<Debt> getCursor(String account1, String accoun2) {
		return db.stream().filter(o -> o.getAccountName1().equals(account1) && o.getAccountName2().equals(accoun2))
				.findFirst();
	}

	@Override
	public Collection<Debt> getList(String accountName) {
		return db.stream().filter(o -> o.getAccountName1().equals(accountName))
				.sorted(Comparator.comparing(Debt::getCreatedAt).reversed())
				.map(Debt::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<Debt> getAllList(String accountName) {
		return db.stream().filter(o -> o.getAccountName1().equals(accountName) || o.getAccountName2().equals(accountName))
				.sorted(Comparator.comparing(Debt::getCreatedAt).reversed())
				.map(Debt::new)
				.collect(Collectors.toList());
	}


	@Override
	public Collection<String> getAccount2List(String accountName){
		return db.stream().filter(o -> o.getAccountName1().equals(accountName))
			.sorted(Comparator.comparing(Debt::getCreatedAt).reversed())
			.map(d -> d.getAccountName2())
			.collect(Collectors.toList());
	}
}
