package com.bank.atm.domain.service.user.command.transfer;

import com.bank.atm.domain.common.ATMException;
import lombok.Getter;

@Getter
public class TransferException extends ATMException {
	private final String source;
	private final String recipient;
	private final int amount;

	protected TransferException(String source, String recipient, int amount, String message) {
		super(message);
		this.source = source;
		this.recipient = recipient;
		this.amount = amount;
	}

	public static TransferException amountMustGreaterThanOrEqualsTo(String source, String recipient, int amount) {
		return new TransferException(source, recipient, amount,
				String.format("your transfer must be greater than or equals to %d", amount));
	}

	public static TransferException cannotTransferToSameAccount(String source, String recipient, int amount) {
		return new TransferException(source, recipient, amount, "you can not transfer into same account");
	}
}