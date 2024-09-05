package com.bank.atm.domain.data.repository;

import java.util.Collection;
import java.util.Optional;

import com.bank.atm.domain.data.model.Debt;

public interface DebtRepository {
	Debt add(Debt debt);

	Collection<Debt> add(Collection<Debt> debts);

	Optional<Debt> update(Debt debt);

	Collection<Debt> update(Collection<Debt> debts);

	Collection<Debt> getList(String accountName);

	Collection<Debt> getAllList(String accountName);

	Collection<String> getAccount2List(String accountName);

	Optional<Debt> get(String accountName1, String accountName2);
}
