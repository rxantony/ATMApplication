package com.dkatalist.atm.domain;

public interface ATMApplication {
    int getId();
    Session getSession();
    void login(String userName);
    AbstractInputHandler getInputHandler();  
}
