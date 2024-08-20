package com.bank.atm.domain.service.user.command.deposit;

import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepositResult {
	private String accountName;
	private int amount;
	private int balance;
	private int requestAmount;
	private Iterable<ReduceDebtResult> paidDebts;
}
