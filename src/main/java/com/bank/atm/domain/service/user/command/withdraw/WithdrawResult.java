package com.bank.atm.domain.service.user.command.withdraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawResult {
	private String accountName;
	private int amount;
	private int balance;
}