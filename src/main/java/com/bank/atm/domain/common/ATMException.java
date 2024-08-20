package com.bank.atm.domain.common;

public class ATMException extends RuntimeException {

	protected ATMException(String message) {
		super(message);
	}

	protected ATMException(Throwable inner) {
		super(inner);
	}

	protected ATMException(String message, Throwable inner) {
		super(message, inner);
	}
}
