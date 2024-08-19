package com.bank.atm.domain.service.user.command.withdraw;

import javax.validation.constraints.NotBlank;

import com.bank.atm.domain.common.ATMException;

import lombok.Getter;

@Getter
public class WithdrawException extends ATMException {
	@NotBlank(message = "accountName is required")
	private String accountName;
	private int amount;

	public WithdrawException(WithdrawCommand command, Throwable cause){
		super(cause);
		accountName = command.getAccountName();
		amount = command.getAmount();
	}

	public WithdrawException(String accountName, int amount, String message) {
		super(message);
		this.accountName = accountName;
		this.amount = amount;
	}

	public static WithdrawException notEnoughAmount(String accountName, int amount) {
		return new WithdrawException(accountName, amount, "your saving account is not sufficient for withdrawal transaction");
	}
}