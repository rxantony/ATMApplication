package com.bank.atm.domain.application;

import com.bank.atm.domain.common.ATMException;

public class SessionExpiredException extends ATMException {
	public SessionExpiredException() {
		super("session is expired");
	}
}
