package com.dkatalist.atm.domain;

public interface ATM {
    int getId();
    Session getSession();
    InputHandler getInputHandler();  
    void login(String userName);
}
