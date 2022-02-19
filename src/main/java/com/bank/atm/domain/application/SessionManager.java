package com.bank.atm.domain.application;

public interface SessionManager {
    Session getSession();

    void login(String userName);

    AbstractInputHandler getInputHandler();
}
