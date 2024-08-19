package com.bank.atm.domain.data.dto;

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
public class AccountDto {

	private String name;

	private int balance;

	public AccountDto(AccountDto acc) {
		name = acc.getName();
		balance = acc.getBalance();
	}
}