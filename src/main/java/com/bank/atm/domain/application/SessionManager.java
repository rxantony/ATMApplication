package com.bank.atm.domain.application;

public interface SessionManager extends AutoCloseable {
    Session getSession();

    boolean hasActiveSession();

    Session login(String userName);

    AbstractInputHandler getInputHandler();
}
