package com.dkatalist.atm.domain.application;

public interface SessionManager {
    Session getSession();
    void login(String userName);
    AbstractInputHandler getInputHandler();  
}
