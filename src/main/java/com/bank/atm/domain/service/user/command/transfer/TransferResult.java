package com.bank.atm.domain.service.user.command.transfer;

import com.bank.atm.domain.service.debt.command.reducedebt.ReduceDebtResult;
import com.bank.atm.domain.service.debt.command.requestdebt.RequestDebtResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResult {
	private String accountName;
	private int amount;
	private int balance;
	private String recipient;
	private RequestDebtResult requestDebt;
	private Iterable<ReduceDebtResult> paidDebts;
}
