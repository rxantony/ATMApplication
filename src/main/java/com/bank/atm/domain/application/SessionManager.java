package com.bank.atm.domain.application;

public interface SessionManager extends AutoCloseable {
    Session getSession();

    void login(String userName);

    AbstractInputHandler getInputHandler();
}
