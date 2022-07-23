package com.bank.atm.domain.application;

public interface SessionManager extends AutoCloseable {
    Session getSession();

    Session login(String userName);

    AbstractInputHandler getInputHandler();
}
