package com.dkatalist.atm.domain;

public interface ATMApplication {
    int getId();
    Session getSession();
    InputHandler getInputHandler();  
    void login(String userName);
}
