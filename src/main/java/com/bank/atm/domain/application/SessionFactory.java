package com.bank.atm.domain.application;

import java.util.function.Consumer;

public interface SessionFactory {
	Session create(String accountName, Consumer<String> logoutCallback);
}
