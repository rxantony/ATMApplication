package com.bank.atm.domain.service.user.command.deposit;

import javax.validation.constraints.NotBlank;

import com.bank.atm.domain.common.ATMException;
import lombok.Getter;

@Getter
public class DepositException extends ATMException {
	@NotBlank
	private final String accountName;
	private final int amount;

	public DepositException(String accountName, int amount, Throwable inner) {
		super(inner);
		this.accountName = accountName;
		this.amount = amount;
	}

	public DepositException(String accountName, int amount, String message) {
		super(message);
		this.accountName = accountName;
		this.amount = amount;
	}

	public static DepositException fromException(String accountName, int amount, Exception inner) {
		return new DepositException(accountName, amount, inner);
	}

	public static DepositException notEnoughAmount(String accountName, int amount) {
		return new DepositException(accountName, amount,
				"your saving account is not sufficient for deposit transaction");
	}

	public static DepositException amountMustGreaterThanOrEqualsTo(String accountName, int amount) {
		return new DepositException(accountName, amount,
				String.format("your deposit must be greater than or equals to %d", amount));
	}

	public static DepositException invalidAcount(String accountName, int amount) {
		return new DepositException(accountName, amount,
				String.format("account with name %s is not exists", amount));
	}
}
